package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ParameterMatcher;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
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
    public boolean accepts(Type type) {
        return typeMatcher.matches(type) && delegate.accepts(type);
    }

    @Override
    public boolean accepts(Parameter parameter) {
        return parameterMatcher.matches(parameter) && delegate.accepts(parameter);
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        return delegate.stub(rootStubber, type);
    }

    @Override
    public Object stub(RootStubber rootStubber, Parameter parameter) {
        return delegate.stub(rootStubber, parameter);
    }
}
