package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ConditionalStubbingStrategy implements StubbingStrategy {

    private final StubbingStrategy delegate;
    private final Matcher<? super Type> typeMatcher;

    ConditionalStubbingStrategy(StubbingStrategy delegate, Matcher<? super Type> typeMatcher) {
        requireNonNull(delegate, "delegate");
        requireNonNull(typeMatcher, "typeMatcher");
        this.delegate = delegate;
        this.typeMatcher = typeMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return typeMatcher.matches(context, type) && delegate.accepts(context, type);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return delegate.stub(context, type);
    }

}
