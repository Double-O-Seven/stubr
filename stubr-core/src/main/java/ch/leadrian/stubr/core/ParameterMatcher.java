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

}
