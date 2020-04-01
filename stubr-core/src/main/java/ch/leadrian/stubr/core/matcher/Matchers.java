package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Collection of factory methods for various default implementations of {@link Matcher}.
 */
public final class Matchers {

    private Matchers() {
    }

    /**
     * Returns a matcher that matches the given {@link Executable} if it accepts the given parameter types.
     * <p>
     * The matcher matches the given {@link Executable} if the number of given parameter types matches the number of
     * parameters of the {@link Executable} and if each parameter type is the same or a subtype of the type of the
     * parameter at the same index. {@code String.class} {@code int.class} would therefore match a method with the
     * signature {@code void foo(CharSequence chars, int value)}.
     *
     * @param parameterTypes parameter types that the {@link Executable} should accept in order for the matcher to
     *                       match
     * @param <T>            subtype of {@link Executable}
     * @return a matcher that matches the given {@link Executable} if it accepts the given parameter types
     */
    public static <T extends Executable> Matcher<T> accepting(Class<?>... parameterTypes) {
        return new ParameterTypesMatcher<>(parameterTypes);
    }

    /**
     * Returns a matcher that matches the given {@link AnnotatedElement} if the element is annotated with an annotation
     * with a simple or qualified name equal to the given {@code annotationName}.
     *
     * @param annotationName simple or qualified annotation name
     * @param <T>            subtype of {@link AnnotatedElement}
     * @return a matcher that matches the given {@link AnnotatedElement} if it is annotated with an annotation name
     * {@code annotationName}
     */
    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(String annotationName) {
        return AnnotationMatcher.by(annotationName);
    }

    /**
     * Returns a matcher that matches the given {@link AnnotatedElement} if the element is annotated with an annotation
     * of type {@code annotationType}.
     *
     * @param annotationType type of the required annotation
     * @param <T>            subtype of {@link AnnotatedElement}
     * @return a matcher that matches the given {@link AnnotatedElement} if the element is annotated with an annotation
     * of type {@code annotationType}
     */
    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(Class<? extends Annotation> annotationType) {
        return AnnotationMatcher.by(annotationType);
    }

    /**
     * Returns a matcher that matches the given {@link AnnotatedElement} if the element is annotated with an annotation
     * equal to the given {@code annotation}.
     *
     * @param annotation the required annotation
     * @param <T>        subtype of {@link AnnotatedElement}
     * @return a matcher that matches the given {@link AnnotatedElement} if the element is annotated with an annotation
     * equal to the given {@code annotation}
     */
    public static <T extends AnnotatedElement> Matcher<T> annotatedWith(Annotation annotation) {
        return AnnotationMatcher.by(annotation);
    }

    /**
     * Returns a matcher that matches {@link AnnotatedElement}s using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches {@link AnnotatedElement}s using a delegate matcher
     */
    public static <T> Matcher<T> annotatedSiteIs(Matcher<? super AnnotatedElement> delegate) {
        return new AnnotatedElementMatcher<>(delegate);
    }

    /**
     * Returns a matcher that always matches.
     *
     * @param <T> the generic type, may be anything
     * @return a matcher that always matches
     */
    public static <T> Matcher<T> any() {
        return (context, value) -> true;
    }

    /**
     * Returns a matcher that matches {@link Executable}s using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches {@link Executable}s using a delegate matcher
     */
    public static <T> Matcher<T> executableIs(Matcher<? super Executable> delegate) {
        return new ExecutableMatcher<>(delegate);
    }

    /**
     * Returns a matcher that matches {@link Constructor}s using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches {@link Constructor}s using a delegate matcher
     */
    public static <T> Matcher<T> constructorIs(Matcher<? super Constructor<?>> delegate) {
        return new ConstructorMatcher<>(delegate);
    }

    /**
     * Returns a matcher that matches {@link Method}s using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches {@link Method}s using a delegate matcher
     */
    public static <T> Matcher<T> methodIs(Matcher<? super Method> delegate) {
        return new MethodMatcher<>(delegate);
    }

    /**
     * Returns a matcher negating the given matcher. The effect is the same as using {@link Matcher#negate()} on the
     * given matcher.
     *
     * @param matcher the matcher to be negated
     * @param <T>     the generic type, may be anything
     * @return a matcher negating the given matcher
     */
    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return matcher.negate();
    }

    /**
     * Returns a matcher that matches {@link AnnotatedElement}s if there is an annotation with a simple name {@code
     * Nullable} present.
     *
     * @param <T> the generic type, may be anything
     * @return a matcher that matches {@link AnnotatedElement}s if there is an annotation with a simple name {@code
     * Nullable} present
     */
    public static <T extends AnnotatedElement> Matcher<T> nullable() {
        return annotatedWith("Nullable");
    }

    /**
     * Returns a matcher that matches {@link AnnotatedElement}s if there is an annotation with a simple name {@code
     * NotNull}, {@code NonNull} or {@code Nonnull} present.
     *
     * @param <T> the generic type, may be anything
     * @return a matcher that matches {@link AnnotatedElement}s if there is an annotation with a simple name {@code *
     * NotNull}, {@code NonNull} or {@code Nonnull} present
     */
    public static <T extends AnnotatedElement> Matcher<T> nonNull() {
        return Matchers.<T>annotatedWith("NotNull")
                .or(annotatedWith("NonNull"))
                .or(annotatedWith("Nonnull"));
    }

    /**
     * Returns a matcher that matches {@link Parameter}s using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches {@link Parameter}s using a delegate matcher
     */
    public static <T> Matcher<T> parameterIs(Matcher<? super Parameter> delegate) {
        return new ParameterMatcher<>(delegate);
    }

    /**
     * Returns a matcher that matches a given value {@code T} in a context of the parent site of the given context
     * using a delegate matcher.
     *
     * @param delegate the delegate
     * @param <T>      the generic type, may be anything
     * @return a matcher that matches a given value {@code T} in a context of the parent site of the given context
     * using a delegate matcher
     */
    public static <T> Matcher<T> parent(Matcher<? super T> delegate) {
        return new ParentMatcher<>(delegate);
    }

}
