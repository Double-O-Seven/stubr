package ch.leadrian.stubr.core.matcher;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class IsDefaultConstructorMatcherTest {

    @Test
    void shouldReturnTrueForConstructorWithoutArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();

        boolean matches = IsDefaultConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnFalseForConstructorWithArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);

        boolean matches = IsDefaultConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @SuppressWarnings("unused")
    private static class Foo {

        Foo(String param1, int param2) {
        }

        Foo() {
        }

    }

}