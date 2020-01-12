package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ParameterMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.TypeMatcher;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ConditionalStubber implements Stubber {

    private final Stubber delegate;
    private final TypeMatcher typeMatcher;
    private final ParameterMatcher parameterMatcher;

    ConditionalStubber(Stubber delegate, TypeMatcher typeMatcher, ParameterMatcher parameterMatcher) {
        requireNonNull(delegate, "delegate");
        requireNonNull(typeMatcher, "typeMatcher");
        requireNonNull(parameterMatcher, "parameterMatcher");
        this.delegate = delegate;
        this.typeMatcher = typeMatcher;
        this.parameterMatcher = parameterMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return typeMatcher.matches(context, type) && delegate.accepts(context, type);
    }

    @Override
    public boolean accepts(StubbingContext context, Parameter parameter) {
        return parameterMatcher.matches(context, parameter) && delegate.accepts(context, parameter);
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
