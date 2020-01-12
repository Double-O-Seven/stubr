package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;

@FunctionalInterface
public interface ParameterMatcher {

    boolean matches(StubbingContext context, Parameter parameter);

    default ParameterMatcher and(ParameterMatcher other) {
        return (context, parameter) -> this.matches(context, parameter) && other.matches(context, parameter);
    }

    default ParameterMatcher or(ParameterMatcher other) {
        return (context, parameter) -> this.matches(context, parameter) || other.matches(context, parameter);
    }

    default ParameterMatcher negate() {
        return (context, parameter) -> !matches(context, parameter);
    }


}
