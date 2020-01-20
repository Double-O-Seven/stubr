package ch.leadrian.stubr.core.matcher;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorMatchersTest {

    @Test
    void shouldReturnAnyConstructorMatcherInstance() {
        assertThat(ConstructorMatchers.any())
                .isEqualTo(AnyConstructorMatcher.INSTANCE);
    }

    @Test
    void shouldReturnIsDefaultConstructorMatcher() {
        assertThat(ConstructorMatchers.isDefault())
                .isEqualTo(IsDefaultConstructorMatcher.INSTANCE);
    }

    @Test
    void shouldReturnIsPublicConstructorMatcher() {
        assertThat(ConstructorMatchers.isPublic())
                .isEqualTo(IsPublicConstructorMatcher.INSTANCE);
    }

    @Test
    void shouldReturnAcceptingConstructorMatcher() {
        assertThat(ConstructorMatchers.accepting(String.class, int.class))
                .isEqualTo(new AcceptingConstructorMatcher(String.class, int.class));
    }

    @Test
    void shouldReturnAnnotatedWithConstructorMatcherByAnnotationName() {
        assertThat(ConstructorMatchers.annotatedWith("Test"))
                .isEqualTo(new AnnotatedWithConstructorMatcher(AnnotationMatcher.by("Test")));
    }

    @Test
    void shouldReturnAnnotatedWithConstructorMatcherByAnnotationType() {
        assertThat(ConstructorMatchers.annotatedWith(Foo.class))
                .isEqualTo(new AnnotatedWithConstructorMatcher(AnnotationMatcher.by(Foo.class)));
    }

    @Test
    void shouldReturnAnnotatedWithConstructorMatcherByAnnotationInstance() {
        Foo annotation = new FooImpl();
        assertThat(ConstructorMatchers.annotatedWith(annotation))
                .isEqualTo(new AnnotatedWithConstructorMatcher(AnnotationMatcher.by(annotation)));
    }

    @interface Foo {
    }

    private static final class FooImpl implements Foo {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Foo.class;
        }
    }

}