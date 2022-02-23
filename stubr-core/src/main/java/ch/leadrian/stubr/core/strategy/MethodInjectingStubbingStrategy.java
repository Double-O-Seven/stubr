/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.core.strategy.Methods.invokeMethodWithStubValues;
import static ch.leadrian.stubr.core.type.Types.visitTypeHierarchy;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

final class MethodInjectingStubbingStrategy extends EnhancingStubbingStrategy {

    private final Matcher<? super Method> matcher;

    MethodInjectingStubbingStrategy(Matcher<? super Method> matcher) {
        requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }

    @Override
    protected Object enhance(StubbingContext context, Type type, Object stubValue) {
        if (stubValue == null) {
            return null;
        }

        Collection<Method> methodsToInject = collectMethodsToInject(context, stubValue.getClass());
        injectMethods(context, stubValue, methodsToInject);
        return stubValue;
    }

    private Collection<Method> collectMethodsToInject(StubbingContext context, Class<?> targetClass) {
        Map<MethodSignature, Method> methodsToInject = new HashMap<>();
        visitTypeHierarchy(targetClass, type -> {
            for (Method method : type.getDeclaredMethods()) {
                if (isStatic(method.getModifiers()) || method.isSynthetic()) {
                    continue;
                }

                if (matcher.matches(context, method)) {
                    methodsToInject.putIfAbsent(new MethodSignature(method), method);
                }
            }
        });
        return methodsToInject.values();
    }

    private void injectMethods(StubbingContext context, Object stubValue, Collection<Method> methodsToInject) {
        methodsToInject.forEach(method -> invokeMethodWithStubValues(context, method, stubValue));
    }

    private static final class MethodSignature {

        private static final EqualsAndHashCode<MethodSignature> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodSignature.class)
                .compareAndHash(signature -> signature.name)
                .compare(signature -> signature.parameterTypes)
                .build();

        private final String name;

        private final List<Class<?>> parameterTypes;

        private MethodSignature(Method method) {
            this.name = method.getName();
            this.parameterTypes = asList(method.getParameterTypes());
        }

        @Override
        public boolean equals(Object o) {
            return EQUALS_AND_HASH_CODE.equals(this, o);
        }

        @Override
        public int hashCode() {
            return EQUALS_AND_HASH_CODE.hashCode(this);
        }

        @Override
        public String toString() {
            return toStringHelper(this)
                    .add("name", name)
                    .add("parameterTypes", parameterTypes)
                    .toString();
        }

    }

}
