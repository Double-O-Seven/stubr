package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public final class Matchers {

    private Matchers() {
    }

    public static <T> Matcher<T> any() {
        return (context, value) -> true;
    }

    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return matcher.negate();
    }

    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(String annotationName) {
        return AnnotationMatcher.by(annotationName);
    }

    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(Class<? extends Annotation> annotationType) {
        return AnnotationMatcher.by(annotationType);
    }

    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(Annotation annotation) {
        return AnnotationMatcher.by(annotation);
    }
}
