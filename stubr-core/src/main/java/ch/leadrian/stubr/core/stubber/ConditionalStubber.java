package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ParameterMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.TypeMatcher;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

final class ConditionalStubber implements Stubber {

    private final Stubber delegate;
    private final TypeMatcher typeMatcher;
    private final ParameterMatcher parameterMatcher;

    ConditionalStubber(Stubber delegate, TypeMatcher typeMatcher, ParameterMatcher parameterMatcher) {
        this.delegate = delegate;
        this.typeMatcher = typeMatcher;
        this.parameterMatcher = parameterMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return typeMatcher.matches(type) && delegate.accepts(context, type);
    }

    @Override
    public boolean accepts(StubbingContext context, Parameter parameter) {
        return parameterMatcher.matches(parameter) && delegate.accepts(context, parameter);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return delegate.stub(context, type);
    }

    @Override
    public Object stub(StubbingContext context, Parameter parameter) {
        return delegate.stub(context, parameter);
    }
}
