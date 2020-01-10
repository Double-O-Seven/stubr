package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.Stubbers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;
import static java.util.Objects.requireNonNull;

final class RootStubberImpl implements RootStubber {

    private final List<Stubber> stubbers;

    private RootStubberImpl(List<Stubber> stubbers) {
        this.stubbers = stubbers;
    }

    @Override
    public Result<?> tryToStub(Type type) {
        for (Stubber stubber : stubbers) {
            if (stubber.accepts(type)) {
                return Result.success(stubber.stub(this, type));
            }
        }
        return Result.failure();
    }

    @Override
    public Object stub(Type type) {
        Result<?> result = tryToStub(type);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    @Override
    public <T> Result<T> tryToStub(Class<T> classToStub) {
        return tryToStub((Type) classToStub).map(classToStub::cast);
    }

    @Override
    public <T> T stub(Class<T> classToStub) {
        return classToStub.cast(stub((Type) classToStub));
    }

    static final class Builder implements RootStubberBuilder {

        private final List<Stubber> stubbers = new ArrayList<>();

        @Override
        public RootStubberBuilder stubber(Stubber stubber) {
            requireNonNull(stubber, "stubber may not be null");
            stubbers.add(stubber);
            return this;
        }

        @Override
        public RootStubberBuilder stubber(Stubber stubber, TypeMatcher matcher) {
            requireNonNull(stubber, "stubber may not be null");
            requireNonNull(matcher, "matcher may not be null");
            return stubber(Stubbers.conditional(stubber, matcher));
        }

        @Override
        public RootStubberBuilder stubber(Stubber stubber, ParameterMatcher matcher) {
            requireNonNull(stubber, "stubber may not be null");
            requireNonNull(matcher, "matcher may not be null");
            return stubber(Stubbers.conditional(stubber, matcher));
        }

        @Override
        public RootStubber build() {
            List<Stubber> reversedStubbers = new ArrayList<>(stubbers);
            reverse(reversedStubbers);
            return new RootStubberImpl(reversedStubbers);
        }
    }
}
