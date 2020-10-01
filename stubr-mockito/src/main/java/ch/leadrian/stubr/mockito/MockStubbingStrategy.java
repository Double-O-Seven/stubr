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
import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

final class MockStubbingStrategy<T> extends SimpleStubbingStrategy<T> {

    private final Class<T> classToMock;
    private final Consumer<? super T> configurationAction;

    MockStubbingStrategy(Class<T> classToMock, Consumer<? super T> configurationAction) {
        requireNonNull(classToMock, "classToMock");
        this.classToMock = classToMock;
        this.configurationAction = configurationAction;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type == classToMock;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return type.getRawType() == classToMock;
    }

    @Override
    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
        return false;
    }

    @Override
    protected T stubClass(StubbingContext context, Class<?> type) {
        return createMock(context);
    }

    @Override
    protected T stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return createMock(context);
    }

    @Override
    protected T stubGenericArrayType(StubbingContext context, GenericArrayType type) {
        throw new StubbingException(context.getSite(), type);
    }

    private T createMock(StubbingContext context) {
        T mock = mock(classToMock, withSettings().defaultAnswer(new StubbingAnswer(context)));
        if (configurationAction != null) {
            configurationAction.accept(mock);
        }
        return mock;
    }

}
