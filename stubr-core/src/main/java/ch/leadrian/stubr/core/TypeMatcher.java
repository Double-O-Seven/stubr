package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.function.Predicate;

@FunctionalInterface
public interface TypeMatcher {

    static TypeMatcher from(Predicate<? super Type> predicate) {
        return predicate::test;
    }

    boolean matches(Type type);

    default TypeMatcher and(TypeMatcher other) {
        return type -> this.matches(type) && other.matches(type);
    }

    default TypeMatcher or(TypeMatcher other) {
        return type -> this.matches(type) || other.matches(type);
    }

    default TypeMatcher negate() {
        return type -> !matches(type);
    }

}
