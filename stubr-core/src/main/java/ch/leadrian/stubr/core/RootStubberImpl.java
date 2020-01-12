package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.Stubbers;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.reverse;
import static java.util.Objects.requireNonNull;

final class RootStubberImpl implements RootStubber {

    private final List<Stubber> stubbers;

    private RootStubberImpl(List<Stubber> stubbers) {
        this.stubbers = stubbers;
    }

    @Override
    public Result<?> tryToStub(Type type, StubbingSite site) {
        StubbingContext context = new StubbingContextImpl(this, site);
        for (Stubber stubber : stubbers) {
            if (stubber.accepts(context, type)) {
                return Result.success(stubber.stub(context, type));
            }
        }
        return Result.failure();
    }

    @Override
    public Result<?> tryToStub(Parameter parameter, StubbingSite site) {
        StubbingContext context = new StubbingContextImpl(this, site);
        for (Stubber stubber : stubbers) {
            if (stubber.accepts(context, parameter)) {
                return Result.success(stubber.stub(context, parameter));
            }
        }
        return Result.failure();
    }

    static final class Builder implements RootStubberBuilder {

        private final List<Stubber> stubbers = new ArrayList<>();

        @Override
        public RootStubberBuilder stubWith(Stubber stubber) {
            requireNonNull(stubber, "stubber may not be null");
            stubbers.add(stubber);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber stubber, TypeMatcher matcher) {
            requireNonNull(stubber, "stubber may not be null");
            requireNonNull(matcher, "matcher may not be null");
            return stubWith(Stubbers.conditional(stubber, matcher));
        }

        @Override
        public RootStubberBuilder stubWith(Stubber stubber, ParameterMatcher matcher) {
            requireNonNull(stubber, "stubber may not be null");
            requireNonNull(matcher, "matcher may not be null");
            return stubWith(Stubbers.conditional(stubber, matcher));
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers) {
            requireNonNull(stubbers, "stubbers may not be null");
            stubbers.forEach(this.stubbers::add);
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, TypeMatcher matcher) {
            requireNonNull(stubbers, "stubbers may not be null");
            requireNonNull(matcher, "matcher may not be null");
            stubbers.forEach(stubber -> stubWith(stubber, matcher));
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, ParameterMatcher matcher) {
            requireNonNull(stubbers, "stubbers may not be null");
            requireNonNull(matcher, "matcher may not be null");
            stubbers.forEach(stubber -> stubWith(stubber, matcher));
            return this;
        }

        @Override
        public RootStubberBuilder stubWith(Stubber... stubbers) {
            requireNonNull(stubbers, "stubbers may not be null");
            addAll(this.stubbers, stubbers);
            return this;
        }

        @Override
        public RootStubber build() {
            List<Stubber> reversedStubbers = new ArrayList<>(stubbers);
            reverse(reversedStubbers);
            return new RootStubberImpl(reversedStubbers);
        }
    }
}
