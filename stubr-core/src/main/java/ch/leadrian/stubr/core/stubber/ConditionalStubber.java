package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ConditionalStubber implements Stubber {

    private final Stubber delegate;
    private final Matcher<? super Type> typeMatcher;

    ConditionalStubber(Stubber delegate, Matcher<? super Type> typeMatcher) {
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
