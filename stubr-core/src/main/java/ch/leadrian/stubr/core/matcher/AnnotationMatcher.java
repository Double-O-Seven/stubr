package ch.leadrian.stubr.core.matcher;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

abstract class AnnotationMatcher<T extends AnnotatedElement> implements Matcher<T> {

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(String annotationName) {
        return new AnnotationMatcher.ByName<>(annotationName);
    }

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(Class<? extends Annotation> annotationType) {
        return new AnnotationMatcher.ByType<>(annotationType);
    }

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(Annotation annotation) {
        return new AnnotationMatcher.ByEquality<>(annotation);
    }

    private AnnotationMatcher() {
    }

    private static final class ByName<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private static final EqualsAndHashCode<ByName> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ByName.class)
                .compareAndHash(matcher -> matcher.annotationName)
                .build();

        private final String annotationName;

        ByName(String annotationName) {
            requireNonNull(annotationName, "annotationName");
            this.annotationName = annotationName;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            return Arrays.stream(value.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(type -> annotationName.equals(type.getName()) || annotationName.equals(type.getSimpleName()));
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

    private static final class ByType<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private static final EqualsAndHashCode<ByType> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ByType.class)
                .compareAndHash(matcher -> matcher.annotationType)
                .build();

        private final Class<? extends Annotation> annotationType;

        ByType(Class<? extends Annotation> annotationType) {
            requireNonNull(annotationType, "annotationType");
            this.annotationType = annotationType;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            return value.isAnnotationPresent(annotationType);
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

    private static final class ByEquality<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private static final EqualsAndHashCode<ByEquality> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ByEquality.class)
                .compareAndHash(matcher -> matcher.annotation)
                .build();

        private final Annotation annotation;

        ByEquality(Annotation annotation) {
            requireNonNull(annotation, "annotation");
            this.annotation = annotation;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            Annotation annotationOnElement = value.getAnnotation(annotation.annotationType());
            return annotation.equals(annotationOnElement);
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
