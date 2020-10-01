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

package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.strategy.SimpleStubbingStrategy;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

final class GenericMockStubbingStrategy extends SimpleStubbingStrategy<Object> {

    static final GenericMockStubbingStrategy FINAL_STUBBING_INSTANCE = new GenericMockStubbingStrategy(true);
    static final GenericMockStubbingStrategy OPEN_ONLY_STUBBING_INSTANCE = new GenericMockStubbingStrategy(false);

    private final boolean stubFinalClasses;

    private GenericMockStubbingStrategy(boolean stubFinalClasses) {
        this.stubFinalClasses = stubFinalClasses;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        if (type.isEnum() || type.isArray() || type.isPrimitive()) {
            return false;
        }
        return stubFinalClasses || !isFinal(type);
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return accepts(context, type.getRawType());
    }

    @Override
    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
        return false;
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return mock(type, withSettings().defaultAnswer(new StubbingAnswer(context)));
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return stub(context, type.getRawType());
    }

    @Override
    protected Object stubGenericArrayType(StubbingContext context, GenericArrayType type) {
        throw new StubbingException(context.getSite(), type);
    }

    private boolean isFinal(Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

}
