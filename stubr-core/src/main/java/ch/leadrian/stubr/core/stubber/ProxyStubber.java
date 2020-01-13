package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.util.Types.getRawType;

final class ProxyStubber implements Stubber {

    static final ProxyStubber CACHING_INSTANCE = new ProxyStubber(true);
    static final ProxyStubber NON_CACHING_INSTANCE = new ProxyStubber(false);

    private final boolean cacheStubs;

    private ProxyStubber(boolean cacheStubs) {
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
                .orElseThrow(IllegalStateException::new);
    }

    private StubbingInvocationHandler getInvocationHandler(StubbingContext context) {
        return cacheStubs ? new CachingInvocationHandler(context) : new SimpleInvocationHandler(context);
    }

    private Object createProxy(Class<?> clazz, StubbingInvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
    }

    private static abstract class StubbingInvocationHandler implements InvocationHandler {

        private final StubbingContext context;

        StubbingInvocationHandler(StubbingContext context) {
            this.context = context;
        }

        protected Object stub(Method method) {
            Type returnType = method.getGenericReturnType();
            if (returnType == void.class || returnType == Void.class || returnType == null) {
                return null;
            }
            return context.getStubber().stub(returnType, StubbingSites.methodReturnValue(context.getSite(), method));
        }

    }

    private static final class SimpleInvocationHandler extends StubbingInvocationHandler {

        SimpleInvocationHandler(StubbingContext context) {
            super(context);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return stub(method);
        }

    }

    private static final class CachingInvocationHandler extends StubbingInvocationHandler {

        private final Map<Method, Object> stubbedValues = new ConcurrentHashMap<>();

        CachingInvocationHandler(StubbingContext context) {
            super(context);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return stubbedValues.computeIfAbsent(method, this::stub);
        }
    }

}
