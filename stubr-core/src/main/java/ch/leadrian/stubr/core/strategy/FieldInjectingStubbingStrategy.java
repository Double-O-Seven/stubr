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

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.site.InjectedFieldStubbingSite;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.site.StubbingSites.injectedField;
import static ch.leadrian.stubr.core.type.Types.trimWildcard;
import static ch.leadrian.stubr.core.type.Types.visitTypeHierarchy;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Objects.requireNonNull;

final class FieldInjectingStubbingStrategy extends EnhancingStubbingStrategy {

    private final Matcher<? super Field> matcher;

    FieldInjectingStubbingStrategy(Matcher<? super Field> matcher) {
        requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }

    @Override
    protected Object enhance(StubbingContext context, Type type, Object stubValue) {
        if (stubValue == null) {
            return null;
        }

        visitTypeHierarchy(stubValue.getClass(), t -> injectFields(context, stubValue, t));
        return stubValue;
    }

    private void injectFields(StubbingContext context, Object stubValue, Class<?> type) {
        if (type.isInterface()) {
            return;
        }

        for (Field field : type.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (isStatic(modifiers) || isFinal(modifiers) || field.isSynthetic()) {
                continue;
            }

            field.setAccessible(true);
            if (matcher.matches(context, field)) {
                injectField(context, stubValue, field);
            }
        }
    }

    private void injectField(StubbingContext context, Object stubValue, Field field) {
        InjectedFieldStubbingSite site = injectedField(context.getSite(), field);
        Type fieldType = trimWildcard(context.getTypeResolver().resolve(field.getGenericType()));
        Object fieldValue = context.getStubber().stub(fieldType, site);
        try {
            field.set(stubValue, fieldValue);
        } catch (IllegalAccessException e) {
            throw new StubbingException(e);
        }
    }

}
