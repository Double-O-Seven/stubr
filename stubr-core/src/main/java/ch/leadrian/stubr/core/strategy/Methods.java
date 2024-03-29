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

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.site.MethodParameterStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.trimWildcard;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;

final class Methods {

    private Methods() {
    }

    static Object invokeStaticMethodWithStubValues(StubbingContext context, Method method) {
        return invokeMethodWithStubValues(context, method, null);
    }

    static Object invokeMethodWithStubValues(StubbingContext context, Method method, Object receiver) {
        boolean isStatic = isStatic(method.getModifiers());
        if (!isStatic && receiver == null) {
            throw new IllegalArgumentException(String.format("Method %s requires a non-null receiver", method));
        }

        Object[] parameterValues = stubParameterValues(context, method);
        return invokeMethod(method, parameterValues, receiver);
    }

    private static Object[] stubParameterValues(StubbingContext context, Method method) {
        return stream(method.getParameters())
                .map(parameter -> stubParameterValue(context, method, parameter))
                .toArray(Object[]::new);
    }

    private static Object stubParameterValue(StubbingContext context, Method method, Parameter parameter) {
        MethodParameterStubbingSite site = StubbingSites.methodParameter(context.getSite(), method, parameter);
        Type parameterType = trimWildcard(context.getTypeResolver().resolve(parameter.getParameterizedType()));
        return context.getStubber().stub(parameterType, site);
    }

    private static Object invokeMethod(Method method, Object[] parameterValues, Object receiver) {
        method.setAccessible(true);
        try {
            return method.invoke(receiver, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new StubbingException(e);
        }
    }

}
