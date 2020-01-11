package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;

final class ProxyStubber implements Stubber {

    private final ClassLoader classLoader;

    ProxyStubber(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public boolean accepts(Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return clazz.isInterface();
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getRawType(), this);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType).map(upperBound -> accept(upperBound, this)).isPresent();
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return false;
            }
        });
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        return accept(type, new TypeVisitor<Object>() {

            @Override
            public Object visit(Class clazz) {
                StubbingInvocationHandler invocationHandler = new StubbingInvocationHandler(rootStubber);
                return Proxy.newProxyInstance(classLoader, new Class<?>[]{clazz}, invocationHandler);
            }

            @Override
            public Object visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getRawType(), this);
            }

            @Override
            public Object visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType)
                        .map(upperBound -> accept(upperBound, this))
                        .orElseThrow(IllegalStateException::new);
            }

            @Override
            public Object visit(TypeVariable typeVariable) {
                throw new IllegalStateException();
            }

        });
    }

    private static final class StubbingInvocationHandler implements InvocationHandler {

        private final RootStubber rootStubber;

        StubbingInvocationHandler(RootStubber rootStubber) {
            this.rootStubber = rootStubber;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Type returnType = method.getGenericReturnType();
            if (returnType == void.class || returnType == Void.class || returnType == null) {
                return null;
            }
            return rootStubber.stub(returnType);
        }
    }

}
