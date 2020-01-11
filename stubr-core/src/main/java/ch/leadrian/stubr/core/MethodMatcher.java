package ch.leadrian.stubr.core;

import java.lang.reflect.Method;
import java.util.function.Predicate;

@FunctionalInterface
public interface MethodMatcher {

    static MethodMatcher from(Predicate<? super Method> predicate) {
        return predicate::test;
    }

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
