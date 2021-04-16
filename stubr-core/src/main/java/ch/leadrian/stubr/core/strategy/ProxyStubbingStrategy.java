/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.internal.com.google.common.base.StandardSystemProperty;

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
import static ch.leadrian.stubr.core.type.Types.trimWildcard;
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
                .map(clazz -> createProxy(clazz, getInvocationHandler(context, clazz)))
                .orElseThrow(() -> new StubbingException(context.getSite(), type));
    }

    private InvocationHandler getInvocationHandler(StubbingContext context, Class<?> type) {
        return cacheStubs ? new CachingInvocationHandler(context, type) : new SimpleInvocationHandler(context, type);
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
        private final Class<?> type;

        StubbingInvocationHandler(StubbingContext context, Class<?> type) {
            this.context = context;
            this.type = type;
        }

        @Override
        public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) {
                return DEFAULT_METHOD_INVOCATION_HANDLER.invoke(proxy, method, args);
            }
            if (isHashCodeMethod(method)) {
                return System.identityHashCode(proxy);
            }
            if (isEqualsMethod(method)) {
                return proxy == args[0];
            }
            if (isToStringMethod(method)) {
                return String.format("Stubbed %s (%s@%s)", type.getName(), proxy.getClass().getName(), Integer.toHexString(System.identityHashCode(proxy)));
            }
            return getReturnValue(method);
        }

        private boolean isHashCodeMethod(Method method) {
            return "hashCode".equals(method.getName()) && method.getParameterCount() == 0;
        }

        private boolean isEqualsMethod(Method method) {
            return "equals".equals(method.getName()) && method.getParameterCount() == 1 && method.getParameterTypes()[0] == Object.class;
        }

        private boolean isToStringMethod(Method method) {
            return "toString".equals(method.getName()) && method.getParameterCount() == 0;
        }

        protected final Object stub(Method method) {
            Type returnType = trimWildcard(context.getTypeResolver().resolve(method.getGenericReturnType()));
            if (returnType == void.class || returnType == Void.class) {
                return null;
            }
            return context.getStubber().stub(returnType, StubbingSites.methodReturnValue(context.getSite(), method));
        }

        protected abstract Object getReturnValue(Method method);

    }

    private static final class SimpleInvocationHandler extends StubbingInvocationHandler {

        SimpleInvocationHandler(StubbingContext context, Class<?> type) {
            super(context, type);
        }

        @Override
        protected Object getReturnValue(Method method) {
            return stub(method);
        }

    }

    private static final class CachingInvocationHandler extends StubbingInvocationHandler {

        private final Map<Method, Object> stubbedValues = new ConcurrentHashMap<>();

        CachingInvocationHandler(StubbingContext context, Class<?> type) {
            super(context, type);
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
