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