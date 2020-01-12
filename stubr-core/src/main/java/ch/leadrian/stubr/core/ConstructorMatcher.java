package ch.leadrian.stubr.core;

import java.lang.reflect.Constructor;

@FunctionalInterface
public interface ConstructorMatcher {

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
