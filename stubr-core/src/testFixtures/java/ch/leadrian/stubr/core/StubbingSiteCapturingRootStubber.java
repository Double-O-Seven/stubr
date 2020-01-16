package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class StubbingSiteCapturingRootStubber extends RootStubber {

    private final List<StubbingSite> capturedSites = new ArrayList<>();
    private final RootStubber delegate;

    StubbingSiteCapturingRootStubber(RootStubber delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }


    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        capturedSites.add(context.getSite());
        return delegate.tryToStub(type, context);
    }

    public List<StubbingSite> getCapturedSites() {
        return capturedSites;
    }
}
