package ch.leadrian.stubr.core.matcher;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.ConstructorMatcher;

import java.lang.reflect.Constructor;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

final class AnnotatedWithConstructorMatcher implements ConstructorMatcher {

    private static final EqualsAndHashCode<AnnotatedWithConstructorMatcher> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(AnnotatedWithConstructorMatcher.class)
            .compareAndHash(matcher -> matcher.annotationMatcher)
            .build();

    private final AnnotationMatcher annotationMatcher;

    AnnotatedWithConstructorMatcher(AnnotationMatcher annotationMatcher) {
        requireNonNull(annotationMatcher, "annotationMatcher");
        this.annotationMatcher = annotationMatcher;
    }

    @Override
    public boolean matches(Constructor<?> constructor) {
        return annotationMatcher.matches(constructor);
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
