package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.Stubbers;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public interface Stubber {

    boolean accepts(StubbingContext context, Type type);

    default boolean accepts(StubbingContext context, Parameter parameter) {
        return accepts(context, parameter.getParameterizedType());
    }

    Object stub(StubbingContext context, Type type);

    default Object stub(StubbingContext context, Parameter parameter) {
        return stub(context, parameter.getParameterizedType());
    }

    default Stubber when(TypeMatcher typeMatcher) {
        return Stubbers.conditional(this, typeMatcher);
    }

    default Stubber when(ParameterMatcher parameterMatcher) {
        return Stubbers.conditional(this, parameterMatcher);
    }

}
