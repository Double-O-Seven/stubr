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

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.List;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.util.Objects.requireNonNull;

final class ParameterTypesMatcher<T extends Executable> implements Matcher<T> {

    private final List<Class<?>> parameterTypes;

    ParameterTypesMatcher(Class<?>... parameterTypes) {
        requireNonNull(parameterTypes, "parameterTypes");
        this.parameterTypes = ImmutableList.copyOf(parameterTypes);
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        Parameter[] parameters = value.getParameters();
        if (parameters.length != parameterTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.size(); i++) {
            if (!isAssignable(parameters[i], parameterTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isAssignable(Parameter parameter, Class<?> type) {
        return getRawType(parameter.getParameterizedType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }

}
