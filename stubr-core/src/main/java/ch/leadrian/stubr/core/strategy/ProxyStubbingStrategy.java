package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.StubbingSites;
import com.google.common.base.StandardSystemProperty;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.lang.Float.parseFloat;
import static java.lang.invoke.MethodHandles.Lookup.PACKAGE;
import static java.lang.invoke.MethodHandles.Lookup.PRIVATE;
import static java.lang.invoke.MethodHandles.Lookup.PROTECTED;
import static java.lang.invoke.MethodHandles.Lookup.PUBLIC;

enum ProxyStubbingStrategy implements StubbingStrategy {
    CACHING(true),
    NON_CACHING(false);

    private final boolean cacheStubs;

    ProxyStubbingStrategy(boolean cacheStubs) {
        this.cacheStubs = cacheStubs;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(Class::isInterface)
                .isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return getRawType(type)
                .map(clazz -> createProxy(clazz, getInvocationHandler(context)))
                .orElseThrow(() -> new StubbingException(context.getSite(), type));
    }

    private InvocationHandler getInvocationHandler(StubbingContext context) {
        return cacheStubs ? new CachingInvocationHandler(context) : new SimpleInvocationHandler(context);
    }

    private Object createProxy(Class<?> clazz, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
    }

    private static abstract class StubbingInvocationHandler implements InvocationHandler {

        private static final InvocationHandler DEFAULT_METHOD_INVOCATION_HANDLER;

        static {
            float version = parseFloat(System.getProperty(StandardSystemProperty.JAVA_CLASS_VERSION.key()));
            boolean isJava8 = version <= 52;
            DEFAULT_METHOD_INVOCATION_HANDLER = isJava8 ? Java8DefaultMethodInvocationHandler.INSTANCE : Java9PlusDefaultMethodInvocationHandler.INSTANCE;
        }

        private final StubbingContext context;

        StubbingInvocationHandler(StubbingContext context) {
            this.context = context;
        }

        @Override
        public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) {
                return DEFAULT_METHOD_INVOCATION_HANDLER.invoke(proxy, method, args);
            }
            return getReturnValue(method);
        }

        protected final Object stub(Method method) {
            Type returnType = method.getGenericReturnType();
            if (returnType == void.class || returnType == Void.class) {
                return null;
            }
            return context.getStubber().stub(returnType, StubbingSites.methodReturnValue(context.getSite(), method));
        }

        protected abstract Object getReturnValue(Method method);

    }

    private static final class SimpleInvocationHandler extends StubbingInvocationHandler {

        SimpleInvocationHandler(StubbingContext context) {
            super(context);
        }

        @Override
        protected Object getReturnValue(Method method) {
            return stub(method);
        }

    }

    private static final class CachingInvocationHandler extends StubbingInvocationHandler {

        private final Map<Method, Object> stubbedValues = new ConcurrentHashMap<>();

        CachingInvocationHandler(StubbingContext context) {
            super(context);
        }

        @Override
        protected Object getReturnValue(Method method) {
            return stubbedValues.computeIfAbsent(method, this::stub);
        }

    }

    private enum Java8DefaultMethodInvocationHandler implements InvocationHandler {
        INSTANCE;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> declaringClass = method.getDeclaringClass();
            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);
            MethodHandles.Lookup lookup = constructor.newInstance(declaringClass, PUBLIC | PROTECTED | PACKAGE | PRIVATE);
            return lookup
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
    }

    private enum Java9PlusDefaultMethodInvocationHandler implements InvocationHandler {
        INSTANCE;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return MethodHandles.lookup()
                    .findSpecial(
                            method.getDeclaringClass(),
                            method.getName(),
                            MethodType.methodType(method.getReturnType(), new Class[0]),
                            method.getDeclaringClass()
                    )
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
    }

}
