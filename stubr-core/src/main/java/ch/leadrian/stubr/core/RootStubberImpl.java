package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.Stubbers;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Objects.requireNonNull;

final class RootStubberImpl implements RootStubber {

    private final List<Stubber> stubbers;

    private RootStubberImpl(List<Stubber> stubbers) {
        this.stubbers = ImmutableList.copyOf(stubbers);
    }

    @Override
    public Result<?> tryToStub(Type type, StubbingSite site) {
        StubbingContext context = new StubbingContext(this, site);
        return stubbers.stream()
                .filter(stubber -> stubber.accepts(context, type))
                .map(stubber -> Result.success(stubber.stub(context, type)))
                .findFirst()
                .orElse(Result.failure());
    }

    static final class Builder implements RootStubberBuilder {

        private final List<Stubber> stubbers = new ArrayList<>();

        @Override
        public RootStubberBuilder stubWith(Stubber stubber) {
            requireNonNull(stubber, "stubber");
            stubbers.add(0, stubber);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber stubber, TypeMatcher matcher) {
            requireNonNull(stubber, "stubber");
            requireNonNull(matcher, "matcher");
            return stubWith(Stubbers.conditional(stubber, matcher));
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers) {
            requireNonNull(stubbers, "stubbers");
            stubbers.forEach(this::stubWith);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, TypeMatcher matcher) {
            requireNonNull(stubbers, "stubbers");
            requireNonNull(matcher, "matcher");
            stubbers.forEach(stubber -> stubWith(stubber, matcher));
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber... stubbers) {
            requireNonNull(stubbers, "stubbers");
            addAll(this.stubbers, stubbers);
            return this;
        }

        @Override
        public RootStubber build() {
            return new RootStubberImpl(stubbers);
        }
    }
}
