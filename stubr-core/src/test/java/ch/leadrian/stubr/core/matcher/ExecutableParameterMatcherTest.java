package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.StubbingContext;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class ExecutableParameterMatcherTest {

    @Test
    void shouldReturnTrueForConstructorWithExactlyMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        ExecutableParameterMatcher<Constructor<?>> matcher = new ExecutableParameterMatcher<>(CharSequence.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnTrueForConstructorWithMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(CharSequence.class, long.class);
        ExecutableParameterMatcher<Constructor<?>> matcher = new ExecutableParameterMatcher<>(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isTrue();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutMatchingArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class, int.class);
        ExecutableParameterMatcher<Constructor<?>> matcher = new ExecutableParameterMatcher<>(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();
        ExecutableParameterMatcher<Constructor<?>> matcher = new ExecutableParameterMatcher<>(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void shouldReturnFalseForConstructorWithoutNumberOfArguments() throws Exception {
        StubbingContext context = mock(StubbingContext.class);
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor(String.class);
        ExecutableParameterMatcher<Constructor<?>> matcher = new ExecutableParameterMatcher<>(String.class, long.class);

        boolean matches = matcher.matches(context, constructor);

        assertThat(matches)
                .isFalse();
    }

    @Test
    void testEquals() {
        new EqualsTester()
                .addEqualityGroup(new ExecutableParameterMatcher<>(String.class, long.class), new ExecutableParameterMatcher<>(String.class, long.class))
                .addEqualityGroup(new ExecutableParameterMatcher<>(long.class, String.class), new ExecutableParameterMatcher<>(long.class, String.class))
                .addEqualityGroup(new ExecutableParameterMatcher<>(Object.class), new ExecutableParameterMatcher<>(Object.class))
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