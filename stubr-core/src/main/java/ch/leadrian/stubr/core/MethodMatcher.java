package ch.leadrian.stubr.core;

import java.lang.reflect.Method;

@FunctionalInterface
public interface MethodMatcher {

    boolean matches(Method Method);

    default MethodMatcher and(MethodMatcher other) {
        return Method -> this.matches(Method) && other.matches(Method);
    }

    default MethodMatcher or(MethodMatcher other) {
        return Method -> this.matches(Method) || other.matches(Method);
    }

    default MethodMatcher negate() {
        return Method -> !matches(Method);
    }

}
