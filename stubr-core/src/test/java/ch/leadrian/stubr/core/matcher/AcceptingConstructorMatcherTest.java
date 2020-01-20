package ch.leadrian.stubr.core.matcher;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("UnstableApiUsage")
class AcceptingConstructorMatcherTest {

    @Test
    void shouldReturnTrueForConstructorWithExactlyMatchingArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        AcceptingConstructorMatcher matcher = new AcceptingConstructorMatcher(CharSequence.class, long.class);

        boolean matches = matcher.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnTrueForConstructorWithMatchingArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        AcceptingConstructorMatcher matcher = new AcceptingConstructorMatcher(String.class, long.class);

        boolean matches = matcher.matches(constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutMatchingArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);
        AcceptingConstructorMatcher matcher = new AcceptingConstructorMatcher(String.class, long.class);

        boolean matches = matcher.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();
        AcceptingConstructorMatcher matcher = new AcceptingConstructorMatcher(String.class, long.class);

        boolean matches = matcher.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutNumberOfArguments() throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class);
        AcceptingConstructorMatcher matcher = new AcceptingConstructorMatcher(String.class, long.class);

        boolean matches = matcher.matches(constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void testEquals() {
        new EqualsTester()
                .addEqualityGroup(new AcceptingConstructorMatcher(String.class, long.class), new AcceptingConstructorMatcher(String.class, long.class))
                .addEqualityGroup(new AcceptingConstructorMatcher(long.class, String.class), new AcceptingConstructorMatcher(long.class, String.class))
                .addEqualityGroup(new AcceptingConstructorMatcher(Object.class), new AcceptingConstructorMatcher(Object.class))
                .addEqualityGroup("test")
                .testEquals();
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