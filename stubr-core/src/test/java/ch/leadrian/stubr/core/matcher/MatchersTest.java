package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.AnnotatedElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MatchersTest {

    @Test
    void anyShouldReturnTrue() {
        Matcher<Object> matcher = Matchers.any();

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void notShouldNegateCondition(boolean inputValue, boolean expectedValue) {
        Matcher<Object> matcher = Matchers.not(((context, value) -> inputValue));

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnAnnotationMatcherByName() {
        Matcher<AnnotatedElement> matcher = Matchers.annotatedWith("Foo");

        assertThat(matcher)
                .isEqualTo(AnnotationMatcher.by("Foo"));
    }

    @Test
    void shouldReturnAnnotationMatcherByType() {
        Matcher<AnnotatedElement> matcher = Matchers.annotatedWith(Foo.class);

        assertThat(matcher)
                .isEqualTo(AnnotationMatcher.by(Foo.class));
    }

    @Test
    void shouldReturnAnnotationMatcherByEquality() {
        Foo annotation = new FooImpl();

        Matcher<AnnotatedElement> matcher = Matchers.annotatedWith(annotation);

        assertThat(matcher)
                .isEqualTo(AnnotationMatcher.by(annotation));
    }

    private @interface Foo {
    }

    private static class FooImpl implements Foo {

        @Override
        public Class<Foo> annotationType() {
            return Foo.class;
        }
    }
}
