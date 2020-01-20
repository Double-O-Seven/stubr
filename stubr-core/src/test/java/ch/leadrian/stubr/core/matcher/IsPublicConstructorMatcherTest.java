package ch.leadrian.stubr.core.matcher;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class IsPublicConstructorMatcherTest {

    @Test
    void givenPublicConstructorItShouldReturnTrue() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class);

        boolean matches = IsPublicConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void givenPackagePrivateConstructorItShouldReturnFalse() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(int.class);

        boolean matches = IsPublicConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void givenProtectedItShouldReturnFalse() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(long.class);

        boolean matches = IsPublicConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void givenPrivateItShouldReturnFalse() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(double.class);

        boolean matches = IsPublicConstructorMatcher.INSTANCE.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @SuppressWarnings("unused")
    private static class Foo {

        public Foo(String param) {
        }

        Foo(int param) {
        }

        protected Foo(long param) {
        }

        private Foo(double param) {
        }

    }

}