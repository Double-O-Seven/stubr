package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;

import static java.util.Objects.requireNonNull;

final class ParentMatcher<T> implements Matcher<T> {

    private final Matcher<? super T> delegate;

    ParentMatcher(Matcher<? super T> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        return context.getSite()
                .getParent()
                .map(parentSite -> StubbingContext.create(context.getStubber(), parentSite))
                .filter(parentContext -> delegate.matches(parentContext, value))
                .isPresent();
    }

}
