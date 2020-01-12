package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface TypeMatcher {

    static TypeMatcher from(BiPredicate<? super StubbingContext, ? super Type> predicate) {
        return predicate::test;
    }

    boolean matches(StubbingContext context, Type type);

    default TypeMatcher and(TypeMatcher other) {
        return (context, type) -> this.matches(context, type) && other.matches(context, type);
    }

    default TypeMatcher or(TypeMatcher other) {
        return (context, type) -> this.matches(context, type) || other.matches(context, type);
    }

    default TypeMatcher negate() {
        return (context, type) -> !matches(context, type);
    }

}
