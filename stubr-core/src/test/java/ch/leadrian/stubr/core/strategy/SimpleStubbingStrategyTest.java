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

import ch.leadrian.stubr.core.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleStubbingStrategyTest {

    @TestFactory
    <T> Stream<DynamicTest> testSimpleStubber() {
        return stubbingStrategyTester()
                .accepts(String.class)
                .andStubs("stubClass")
                .accepts(new TypeLiteral<List<String>>() {})
                .andStubs(singletonList("stubParameterizedType"))
                .accepts(new ParameterizedTypeLiteral<List<? extends String>>() {}.getActualTypeArgument(0))
                .andStubs("stubClass")
                .accepts(new ParameterizedTypeLiteral<List<? super String>>() {}.getActualTypeArgument(0))
                .andStubs("stubClass")
                .accepts(new ParameterizedTypeLiteral<List<? extends List<String>>>() {}.getActualTypeArgument(0))
                .andStubs(singletonList("stubParameterizedType"))
                .accepts(new ParameterizedTypeLiteral<List<? super List<String>>>() {}.getActualTypeArgument(0))
                .andStubs(singletonList("stubParameterizedType"))
                .accepts(new TypeLiteral<List<String>[]>() {}.getType())
                .andStubs(new List[]{singletonList("stubGenericArrayType")})
                .rejects(Object.class)
                .rejects(new TypeLiteral<List<Object>>() {})
                .rejects(new TypeLiteral<T>() {})
                .rejects(new ParameterizedTypeLiteral<List<?>>() {}.getActualTypeArgument(0))
                .rejects(new ParameterizedTypeLiteral<List<? extends T>>() {}.getActualTypeArgument(0))
                .rejects(new ParameterizedTypeLiteral<List<? super T>>() {}.getActualTypeArgument(0))
                .test(new SimpleStubbingStrategy<Object>() {

                    @Override
                    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
                        return type == String.class;
                    }

                    @Override
                    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
                        return new TypeLiteral<List<String>>() {}.getType().equals(type);
                    }

                    @Override
                    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
                        return new TypeLiteral<List<String>[]>() {}.getType().equals(type);
                    }

                    @Override
                    protected Object stubClass(StubbingContext context, Class<?> type) {
                        return "stubClass";
                    }

                    @Override
                    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
                        return singletonList("stubParameterizedType");
                    }

                    @Override
                    protected Object stubGenericArrayType(StubbingContext context, GenericArrayType type) {
                        return new List[]{singletonList("stubGenericArrayType")};
                    }
                });
    }

    @Test
    <T> void givenTypeVariableStubShouldThrowException() {
        StubbingSite site = mock(StubbingSite.class);
        StubbingContext context = mock(StubbingContext.class);
        when(context.getSite())
                .thenReturn(site);
        Type type = new TypeLiteral<T>() {}.getType();
        StubbingStrategy stubbingStrategy = new SimpleStubbingStrategy<Object>() {

            @Override
            protected boolean acceptsClass(StubbingContext context, Class<?> type) {
                return false;
            }

            @Override
            protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
                return false;
            }

            @Override
            protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
                return false;
            }

            @Override
            protected Object stubClass(StubbingContext context, Class<?> type) {
                return null;
            }

            @Override
            protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
                return null;
            }

            @Override
            protected Object stubGenericArrayType(StubbingContext context, GenericArrayType type) {
                return null;
            }
        };

        Throwable caughtThrowable = catchThrowable(() -> stubbingStrategy.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOfSatisfying(StubbingException.class, exception -> assertAll(
                        () -> assertThat(exception.getSite()).hasValue(site),
                        () -> assertThat(exception.getType()).hasValue(type)
                ));
    }

}