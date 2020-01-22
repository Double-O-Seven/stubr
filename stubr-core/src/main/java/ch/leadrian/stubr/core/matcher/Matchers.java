package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class Matchers {

    private Matchers() {
    }

    public static <T extends Executable> Matcher<T> accepting(Class<?>... parameterTypes) {
        return new ParameterTypesMatcher<>(parameterTypes);
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

    public static <T> Matcher<T> annotatedSiteIs(Matcher<? super AnnotatedElement> delegate) {
        return new AnnotatedElementMatcher<>(delegate);
    }

    public static <T> Matcher<T> any() {
        return (context, value) -> true;
    }

    public static <T> Matcher<T> constructorIs(Matcher<? super Constructor<?>> delegate) {
        return new ConstructorMatcher<>(delegate);
    }

    public static <T> Matcher<T> methodIs(Matcher<? super Method> delegate) {
        return new MethodMatcher<>(delegate);
    }

    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return matcher.negate();
    }

    public static <T extends AnnotatedElement> Matcher<T> nullable() {
        return annotatedWith("Nullable");
    }

    public static <T extends AnnotatedElement> Matcher<T> nonNull() {
        return Matchers.<T>annotatedWith("NotNull")
                .or(annotatedWith("NonNull"))
                .or(annotatedWith("Nonnull"));
    }

    public static <T> Matcher<T> parameterIs(Matcher<? super Parameter> delegate) {
        return new ParameterMatcher<>(delegate);
    }

}
