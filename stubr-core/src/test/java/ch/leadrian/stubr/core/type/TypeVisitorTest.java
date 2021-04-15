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

package ch.leadrian.stubr.core.type;

import ch.leadrian.stubr.core.ParameterizedTypeLiteral;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TypeVisitorTest {

    @Test
    void shouldVisitClass() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);

        TypeVisitor.accept(String.class, visitor);

        verify(visitor).visit(String.class);
    }

    @Test
    void shouldVisitParameterizedType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new TypeLiteral<List<String>>() {}.getType();

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((ParameterizedType) type);
    }

    @Test
    void shouldVisitWildcardType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new ParameterizedTypeLiteral<List<?>>() {}.getActualTypeArgument(0);

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((WildcardType) type);
    }

    @Test
    <T> void shouldVisitTypeVariable() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new TypeLiteral<T>() {}.getType();

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((TypeVariable<?>) type);
    }

    @Test
    <T> void shouldVisitGenericArrayType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new TypeLiteral<T[]>() {}.getType();

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((GenericArrayType) type);
    }

    @Test
    void givenUnknownTypeItShouldThrowException() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new UnknownType();

        Throwable caughtThrowable = catchThrowable(() -> TypeVisitor.accept(type, visitor));

        assertThat(caughtThrowable)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported type: UnknownType");
    }

    private static final class UnknownType implements Type {

        @Override
        public String toString() {
            return "UnknownType";
        }

    }

}