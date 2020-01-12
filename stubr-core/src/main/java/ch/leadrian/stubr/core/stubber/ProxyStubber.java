package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.util.Types.getActualClass;
import static java.util.Objects.requireNonNull;

final class ProxyStubber implements Stubber {

    private final ClassLoader classLoader;

    ProxyStubber(ClassLoader classLoader) {
        requireNonNull(classLoader, "classLoader");
        this.classLoader = classLoader;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getActualClass(type)
                .filter(Class::isInterface)
                .isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return getActualClass(type)
                .map(clazz -> {
                    StubbingInvocationHandler invocationHandler = new StubbingInvocationHandler(context);
                    return Proxy.newProxyInstance(classLoader, new Class<?>[]{clazz}, invocationHandler);
                })
                .orElseThrow(IllegalStateException::new);
    }

    private static final class StubbingInvocationHandler implements InvocationHandler {

        private final StubbingContext context;

        StubbingInvocationHandler(StubbingContext context) {
            this.context = context;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Type returnType = method.getGenericReturnType();
            if (returnType == void.class || returnType == Void.class || returnType == null) {
                return null;
            }
            return context.getStubber().stub(returnType, StubbingSites.methodReturnValue(context.getSite(), method));
        }
    }

}
