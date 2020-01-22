package ch.leadrian.stubr.core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ch.leadrian.stubr.core.stubber.Stubbers.conditional;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

final class RootStubberImpl extends RootStubber {

    private final List<Stubber> stubbers;

    private RootStubberImpl(List<Stubber> stubbers) {
        this.stubbers = ImmutableList.copyOf(stubbers);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        return stubbers.stream()
                .filter(stubber -> stubber.accepts(context, type))
                .map(stubber -> Result.success(stubber.stub(context, type)))
                .findFirst()
                .orElse(Result.failure());
    }

    static final class Builder implements RootStubberBuilder {

        private final List<RootStubber> rootStubbers = new ArrayList<>();
        private final List<Stubber> stubbers = new ArrayList<>();

        @Override
        public RootStubberBuilder include(RootStubber rootStubber) {
            requireNonNull(rootStubber, "rootStubber");
            rootStubbers.add(0, rootStubber);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber stubber) {
            requireNonNull(stubber, "stubber");
            stubbers.add(0, stubber);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber stubber, Matcher<? super Type> matcher) {
            requireNonNull(stubber, "stubber");
            requireNonNull(matcher, "matcher");
            return stubWith(conditional(stubber, matcher));
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers) {
            requireNonNull(stubbers, "stubbers");
            stubbers.forEach(this::stubWith);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, Matcher<? super Type> matcher) {
            requireNonNull(stubbers, "stubbers");
            requireNonNull(matcher, "matcher");
            stubbers.forEach(stubber -> stubWith(stubber, matcher));
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber... stubbers) {
            requireNonNull(stubbers, "stubbers");
            stubWith(asList(stubbers));
            return this;
        }

        @Override
        public RootStubber build() {
            RootStubber builtRootStubber = new RootStubberImpl(stubbers);
            if (rootStubbers.isEmpty()) {
                return builtRootStubber;
            } else {
                List<RootStubber> rootStubberComposition = new ArrayList<>(this.rootStubbers);
                rootStubberComposition.add(0, builtRootStubber);
                return compose(rootStubberComposition);
            }
        }
    }
}
