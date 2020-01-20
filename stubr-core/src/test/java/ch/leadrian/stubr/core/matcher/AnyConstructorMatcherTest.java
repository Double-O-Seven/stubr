package ch.leadrian.stubr.core.matcher;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class AnyConstructorMatcherTest {

    @Test
    void shouldReturnTrueForConstructorWithoutArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();

        boolean matches = AnyConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnTrueForConstructorWithArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);

        boolean matches = AnyConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @SuppressWarnings("unused")
    private static class Foo {

        Foo(String param1, int param2) {
        }

        Foo() {
        }

    }

}