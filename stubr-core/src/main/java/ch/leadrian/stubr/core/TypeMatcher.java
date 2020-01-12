package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

@FunctionalInterface
public interface TypeMatcher {

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
