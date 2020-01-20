package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;

import java.lang.annotation.Annotation;

public final class ConstructorMatchers {

    private ConstructorMatchers() {
    }

    public static ConstructorMatcher any() {
        return AnyConstructorMatcher.INSTANCE;
    }

    public static ConstructorMatcher isDefault() {
        return IsDefaultConstructorMatcher.INSTANCE;
    }

    public static ConstructorMatcher accepting(Class<?>... parameterTypes) {
        return new AcceptingConstructorMatcher(parameterTypes);
    }

    public static ConstructorMatcher annotatedWith(String annotationName) {
        return new AnnotatedWithConstructorMatcher(AnnotationMatcher.by(annotationName));
    }

    public static ConstructorMatcher annotatedWith(Class<? extends Annotation> annotationType) {
        return new AnnotatedWithConstructorMatcher(AnnotationMatcher.by(annotationType));
    }

    public static ConstructorMatcher annotatedWith(Annotation annotation) {
        return new AnnotatedWithConstructorMatcher(AnnotationMatcher.by(annotation));
    }

}
