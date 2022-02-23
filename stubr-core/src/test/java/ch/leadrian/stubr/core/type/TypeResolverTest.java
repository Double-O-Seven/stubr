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

package ch.leadrian.stubr.core.type;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;

class TypeResolverTest {

    @Test
    void shouldResolveTypeParameterOfClass() {
        TypeLiteral<Foo<String>> typeLiteral = new TypeLiteral<Foo<String>>() {
        };
        TypeResolver resolver = TypeResolver.using(typeLiteral);

        Type resolvedType = resolver.resolve(Foo.class.getTypeParameters()[0]);

        assertThat(resolvedType)
                .isEqualTo(String.class);
    }

    @Test
    void shouldResolveGenericMethodParameter() throws NoSuchMethodException {
        TypeLiteral<Foo<String>> typeLiteral = new TypeLiteral<Foo<String>>() {
        };
        TypeResolver resolver = TypeResolver.using(typeLiteral);
        Method fooMethod = Foo.class.getMethod("foo", Object.class, int.class);

        Type resolvedType = resolver.resolve(fooMethod.getGenericParameterTypes()[0]);

        assertThat(resolvedType)
                .isEqualTo(String.class);
    }

    @Test
    void shouldResolveNonGenericMethodParameter() throws NoSuchMethodException {
        TypeLiteral<Foo<String>> typeLiteral = new TypeLiteral<Foo<String>>() {
        };
        TypeResolver resolver = TypeResolver.using(typeLiteral);
        Method fooMethod = Foo.class.getMethod("foo", Object.class, int.class);

        Type resolvedType = resolver.resolve(fooMethod.getGenericParameterTypes()[1]);

        assertThat(resolvedType)
                .isEqualTo(int.class);
    }

    @Test
    void shouldResolveTypeItself() throws NoSuchMethodException {
        TypeLiteral<Foo<String>> typeLiteral = new TypeLiteral<Foo<String>>() {
        };
        TypeResolver resolver = TypeResolver.using(typeLiteral);
        Method selfMethod = Foo.class.getMethod("self");

        Type resolvedType = resolver.resolve(selfMethod.getGenericReturnType());

        assertThat(resolvedType)
                .isEqualTo(typeLiteral.getType());
    }

    @Test
    void shouldResolveTypeParameterOfSubclass() {
        TypeResolver resolver = TypeResolver.using(Fubar.class);

        Type resolvedType = resolver.resolve(Foo.class.getTypeParameters()[0]);

        assertThat(resolvedType)
                .isEqualTo(Integer.class);
    }

    @Test
    void shouldResolveGenericMethodParameterForSubclass() throws NoSuchMethodException {
        TypeResolver resolver = TypeResolver.using(Fubar.class);
        Method fooMethod = Foo.class.getMethod("foo", Object.class, int.class);

        Type resolvedType = resolver.resolve(fooMethod.getGenericParameterTypes()[0]);

        assertThat(resolvedType)
                .isEqualTo(Integer.class);
    }

    @Test
    void shouldResolveNonGenericMethodParameterForSubclass() throws NoSuchMethodException {
        TypeResolver resolver = TypeResolver.using(Fubar.class);
        Method fooMethod = Foo.class.getMethod("foo", Object.class, int.class);

        Type resolvedType = resolver.resolve(fooMethod.getGenericParameterTypes()[1]);

        assertThat(resolvedType)
                .isEqualTo(int.class);
    }

    static class Foo<T> {

        @SuppressWarnings("unused")
        public void foo(T value1, int value2) {
        }

        public Foo<T> self() {
            return this;
        }

    }

    static class Fubar extends Foo<Integer> {
    }

}