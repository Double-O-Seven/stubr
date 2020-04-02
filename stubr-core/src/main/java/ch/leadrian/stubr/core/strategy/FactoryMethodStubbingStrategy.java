/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.MethodParameterStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class FactoryMethodStubbingStrategy implements StubbingStrategy {

    private final Selector<Method> methodSelector;
    private final Map<Class<?>, Optional<Method>> factoryMethodsByClass = new ConcurrentHashMap<>();

    FactoryMethodStubbingStrategy(Selector<Method> methodSelector) {
        requireNonNull(methodSelector, "methodSelector");
        this.methodSelector = methodSelector;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getFactoryMethod(context, type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Method method = getFactoryMethod(context, type)
                .orElseThrow(() -> new StubbingException("No matching factory method found", context.getSite(), type));
        Object[] parameterValues = stub(context, method);
        return invokeFactoryMethod(method, parameterValues);
    }

    private Object invokeFactoryMethod(Method method, Object[] parameterValues) {
        method.setAccessible(true);
        try {
            return method.invoke(null, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object[] stub(StubbingContext context, Method method) {
        return stream(method.getParameters())
                .map(parameter -> stub(context, method, parameter))
                .toArray(Object[]::new);
    }

    private Object stub(StubbingContext context, Method method, Parameter parameter) {
        MethodParameterStubbingSite site = StubbingSites.methodParameter(context.getSite(), method, parameter);
        return context.getStubber().stub(parameter.getParameterizedType(), site);
    }

    private Optional<Method> getFactoryMethod(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(this::isInstantiable)
                .flatMap(rawType -> getFactoryMethod(context, rawType));
    }

    private boolean isInstantiable(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !clazz.isEnum()
                && !clazz.isInterface();
    }

    private Optional<Method> getFactoryMethod(StubbingContext context, Class<?> targetClass) {
        return factoryMethodsByClass.computeIfAbsent(targetClass, c -> {
            List<Method> methods = getFactoryMethods(targetClass);
            return methodSelector.select(context, methods);
        });
    }

    private List<Method> getFactoryMethods(Class<?> targetClass) {
        return stream(targetClass.getDeclaredMethods())
                .filter(method -> !method.isSynthetic() && !isPrivate(method.getModifiers()) && isStatic(method.getModifiers()))
                .filter(method -> canReturn(method, targetClass))
                .collect(toList());
    }

    private boolean canReturn(Method method, Class<?> type) {
        return getRawType(method.getGenericReturnType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }

}