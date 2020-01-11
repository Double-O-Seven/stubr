package ch.leadrian.stubr.core;

import java.lang.reflect.Constructor;
import java.util.function.Predicate;

@FunctionalInterface
public interface ConstructorMatcher {

    static ConstructorMatcher from(Predicate<? super Constructor<?>> predicate) {
        return predicate::test;
    }

    boolean matches(Constructor<?> constructor);

    default ConstructorMatcher and(ConstructorMatcher other) {
        return constructor -> this.matches(constructor) && other.matches(constructor);
    }

    default ConstructorMatcher or(ConstructorMatcher other) {
        return constructor -> this.matches(constructor) || other.matches(constructor);
    }

    default ConstructorMatcher negate() {
        return constructor -> !matches(constructor);
    }

}
