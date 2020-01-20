package ch.leadrian.stubr.core.matcher;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
class AnnotatedWithConstructorMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnTrueIfAndOnlyIfAnnotationMatcherReturnsTrue(boolean expectedResult) throws Exception {
        Constructor<Foo> constructor = Foo.class.getDeclaredConstructor();
        AnnotationMatcher annotationMatcher = mock(AnnotationMatcher.class);
        when(annotationMatcher.matches(constructor))
                .thenReturn(expectedResult);
        AnnotatedWithConstructorMatcher matcher = new AnnotatedWithConstructorMatcher(annotationMatcher);

        boolean result = matcher.matches(constructor);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void testEquals() {
        AnnotationMatcher annotationMatcher1 = mock(AnnotationMatcher.class);
        AnnotationMatcher annotationMatcher2 = mock(AnnotationMatcher.class);

        new EqualsTester()
                .addEqualityGroup(new AnnotatedWithConstructorMatcher(annotationMatcher1), new AnnotatedWithConstructorMatcher(annotationMatcher1))
                .addEqualityGroup(new AnnotatedWithConstructorMatcher(annotationMatcher2), new AnnotatedWithConstructorMatcher(annotationMatcher2))
                .addEqualityGroup("Test")
                .testEquals();
    }

    private static class Foo {

        Foo() {
        }

    }

}