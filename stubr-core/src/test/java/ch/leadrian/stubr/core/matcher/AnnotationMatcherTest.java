package ch.leadrian.stubr.core.matcher;

import ch.leadrian.equalizer.Equalizer;
import ch.leadrian.equalizer.EqualsAndHashCode;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("UnstableApiUsage")
class AnnotationMatcherTest {

    @Test
    void givenAnnotationWithSimpleNameIsPresentItShouldReturnTrue() {
        AnnotationMatcher matcher = AnnotationMatcher.by("Foo");

        boolean result = matcher.matches(Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationWithQualifiedNameIsPresentItShouldReturnTrue() {
        AnnotationMatcher matcher = AnnotationMatcher.by("ch.leadrian.stubr.core.matcher.AnnotationMatcherTest$Foo");

        boolean result = matcher.matches(Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenNoAnnotationWithSimpleNameIsPresentItShouldReturnFalse() {
        AnnotationMatcher matcher = AnnotationMatcher.by("Foo");

        boolean result = matcher.matches(Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenNoAnnotationWithQualifiedNameIsPresentItShouldReturnFalse() {
        AnnotationMatcher matcher = AnnotationMatcher.by("ch.leadrian.stubr.core.matcher.AnnotationMatcherTest$Foo");

        boolean result = matcher.matches(Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenAnnotationIsPresentItShouldReturnTrue() {
        AnnotationMatcher matcher = AnnotationMatcher.by(Foo.class);

        boolean result = matcher.matches(Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationIsNotPresentItShouldReturnFalse() {
        AnnotationMatcher matcher = AnnotationMatcher.by(Foo.class);

        boolean result = matcher.matches(Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenEqualAnnotationIsPresentItShouldReturnTrue() {
        AnnotationMatcher matcher = AnnotationMatcher.by(new FooImpl("test"));

        boolean result = matcher.matches(Bar.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void givenAnnotationWithDifferentValueIsPresentItShouldReturnFalse() {
        AnnotationMatcher matcher = AnnotationMatcher.by(new FooImpl("bla"));

        boolean result = matcher.matches(Bar.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void givenEqualAnnotationIsNotPresentItShouldReturnFalse() {
        AnnotationMatcher matcher = AnnotationMatcher.by(new FooImpl("test"));

        boolean result = matcher.matches(Qux.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void testEquals() {
        new EqualsTester()
                .addEqualityGroup(AnnotationMatcher.by(Foo.class), AnnotationMatcher.by(Foo.class))
                .addEqualityGroup(AnnotationMatcher.by(Bla.class), AnnotationMatcher.by(Bla.class))
                .addEqualityGroup(AnnotationMatcher.by(new FooImpl("test")), AnnotationMatcher.by(new FooImpl("test")))
                .addEqualityGroup(AnnotationMatcher.by(new FooImpl("lol")), AnnotationMatcher.by(new FooImpl("lol")))
                .addEqualityGroup(AnnotationMatcher.by("Foo"), AnnotationMatcher.by("Foo"))
                .addEqualityGroup(AnnotationMatcher.by("Bla"), AnnotationMatcher.by("Bla"))
                .addEqualityGroup("Test")
                .testEquals();
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

        private static final EqualsAndHashCode<Foo> EQUALS_AND_HASH_CODE = Equalizer.equalsAndHashCodeBuilder(Foo.class)
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