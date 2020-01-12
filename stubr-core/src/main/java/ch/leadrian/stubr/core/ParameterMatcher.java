package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface ParameterMatcher {

    static ParameterMatcher from(TypeMatcher typeMatcher) {
        return (context, parameter) -> typeMatcher.matches(context, parameter.getParameterizedType());
    }

    static ParameterMatcher from(BiPredicate<? super StubbingContext, ? super Parameter> predicate) {
        return predicate::test;
    }

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
