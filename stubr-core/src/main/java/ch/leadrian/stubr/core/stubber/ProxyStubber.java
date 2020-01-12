package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.util.Types.getActualClass;

final class ProxyStubber implements Stubber {

    static final ProxyStubber INSTANCE = new ProxyStubber();

    private ProxyStubber() {
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
                    return createProxy(clazz, invocationHandler);
                })
                .orElseThrow(IllegalStateException::new);
    }

    private Object createProxy(Class<?> clazz, StubbingInvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
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
