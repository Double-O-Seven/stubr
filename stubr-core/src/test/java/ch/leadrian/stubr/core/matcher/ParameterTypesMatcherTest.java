/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ParameterTypesMatcherTest {

    @Test
    void shouldReturnTrueForConstructorWithExactlyMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        Matcher<Constructor<?>> matcher = Matchers.accepting(CharSequence.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnTrueForConstructorWithMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        Matcher<Constructor<?>> matcher = Matchers.accepting(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);
        Matcher<Constructor<?>> matcher = Matchers.accepting(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();
        Matcher<Constructor<?>> matcher = Matchers.accepting(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutNumberOfArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class);
        Matcher<Constructor<?>> matcher = Matchers.accepting(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @SuppressWarnings("unused")
    private static class Foo {

        Foo(CharSequence param1, long param2) {
        }

        Foo(String param1, int param2) {
        }

        Foo() {
        }

        Foo(String param1) {
        }

    }

}