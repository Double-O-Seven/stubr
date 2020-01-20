package ch.leadrian.stubr.core.matcher;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorMatchersTest {

    @Nested
    class AnyTest {

        @Test
        void shouldReturnTrueForConstructorWithoutArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();

            boolean matches = ConstructorMatchers.any().matches(constructor);

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void shouldReturnTrueForConstructorWithArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);

            boolean matches = ConstructorMatchers.any().matches(constructor);

            assertThat(matches)
                    .isTrue();
        }

    }

    @Nested
    class IsDefaultTest {

        @Test
        void shouldReturnTrueForConstructorWithoutArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();

            boolean matches = ConstructorMatchers.isDefault().matches(constructor);

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void shouldReturnFalseForConstructorWithArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);

            boolean matches = ConstructorMatchers.isDefault().matches(constructor);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nested
    class AcceptingTest {

        @Test
        void shouldReturnTrueForConstructorWithExactlyMatchingArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);

            boolean matches = ConstructorMatchers.accepting(CharSequence.class, long.class).matches(constructor);

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void shouldReturnTrueForConstructorWithMatchingArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);

            boolean matches = ConstructorMatchers.accepting(String.class, long.class).matches(constructor);

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void shouldReturnFalseForConstructorWithoutMatchingArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);

            boolean matches = ConstructorMatchers.accepting(String.class, long.class).matches(constructor);

            assertThat(matches)
                    .isFalse();
        }

        @Test
        void shouldReturnFalseForConstructorWithoutArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();

            boolean matches = ConstructorMatchers.accepting(String.class, long.class).matches(constructor);

            assertThat(matches)
                    .isFalse();
        }

        @Test
        void shouldReturnFalseForConstructorWithoutNumberOfArguments() throws Exception {
            Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class);

            boolean matches = ConstructorMatchers.accepting(String.class, long.class).matches(constructor);

            assertThat(matches)
                    .isFalse();
        }

    }

    @SuppressWarnings("unused")
    private static class Foo {

        Foo(String param1) {
        }

        Foo(String param1, int param2) {
        }

        Foo(String param1, float param2) {
        }

        Foo(CharSequence param1, long param2) {
        }

        Foo() {
        }

    }

}