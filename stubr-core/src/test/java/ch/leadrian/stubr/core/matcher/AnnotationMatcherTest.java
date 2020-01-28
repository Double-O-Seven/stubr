package ch.leadrian.stubr.core.matcher;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AnnotationMatcherTest {

    @Test
    void givenAnnotationWithSimpleNameIsPresentItShouldReturnTrue() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith("Foo");

        boolean result = matcher.matches(context, Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationWithQualifiedNameIsPresentItShouldReturnTrue() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith("ch.leadrian.stubr.core.matcher.AnnotationMatcherTest$Foo");

        boolean result = matcher.matches(context, Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenNoAnnotationWithSimpleNameIsPresentItShouldReturnFalse() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith("Foo");

        boolean result = matcher.matches(context, Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenNoAnnotationWithQualifiedNameIsPresentItShouldReturnFalse() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith("ch.leadrian.stubr.core.matcher.AnnotationMatcherTest$Foo");

        boolean result = matcher.matches(context, Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenAnnotationIsPresentItShouldReturnTrue() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith(Foo.class);

        boolean result = matcher.matches(context, Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationIsNotPresentItShouldReturnFalse() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith(Foo.class);

        boolean result = matcher.matches(context, Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenEqualAnnotationIsPresentItShouldReturnTrue() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith(new FooImpl("test"));

        boolean result = matcher.matches(context, Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationWithDifferentValueIsPresentItShouldReturnFalse() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith(new FooImpl("bla"));

        boolean result = matcher.matches(context, Bar.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenEqualAnnotationIsNotPresentItShouldReturnFalse() {
        StubbingContext context = mock(StubbingContext.class);
        Matcher<Class<?>> matcher = Matchers.annotatedWith(new FooImpl("test"));

        boolean result = matcher.matches(context, Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Foo("test")
    private static class Bar {
    }

    @Bla
    private static class Qux {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Foo {

        String value();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Bla {
    }

    private static final class FooImpl implements Foo {

        private static final EqualsAndHashCode<Foo> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Foo.class)
                .compareAndHash(Foo::value)
                .build();

        private final String value;

        private FooImpl(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<Foo> annotationType() {
            return Foo.class;
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS_AND_HASH_CODE.equals(this, obj);
        }

        @Override
        public int hashCode() {
            return EQUALS_AND_HASH_CODE.hashCode(this);
        }

    }

}