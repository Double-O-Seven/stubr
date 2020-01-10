package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;
import java.util.function.Predicate;

@FunctionalInterface
public interface ParameterMatcher {

    static ParameterMatcher from(TypeMatcher typeMatcher) {
        return parameter -> typeMatcher.matches(parameter.getParameterizedType());
    }

    static ParameterMatcher from(Predicate<? super Parameter> predicate) {
        return predicate::test;
    }

    boolean matches(Parameter parameter);

    default ParameterMatcher and(ParameterMatcher other) {
        return parameter -> this.matches(parameter) && other.matches(parameter);
    }

    default ParameterMatcher or(ParameterMatcher other) {
        return parameter -> this.matches(parameter) || other.matches(parameter);
    }

    default ParameterMatcher negate() {
        return parameter -> !matches(parameter);
    }


}
