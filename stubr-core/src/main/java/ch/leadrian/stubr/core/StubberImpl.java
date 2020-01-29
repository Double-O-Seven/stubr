package ch.leadrian.stubr.core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ch.leadrian.stubr.core.strategy.StubbingStrategies.conditional;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

final class StubberImpl extends Stubber {

    private final List<StubbingStrategy> strategies;

    private StubberImpl(List<StubbingStrategy> strategies) {
        this.strategies = ImmutableList.copyOf(strategies);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        return strategies.stream()
                .filter(strategy -> strategy.accepts(context, type))
                .map(strategy -> Result.success(strategy.stub(context, type)))
                .findFirst()
                .orElse(Result.failure());
    }

    static final class Builder implements StubberBuilder {

        private final List<Stubber> stubbers = new ArrayList<>();
        private final List<StubbingStrategy> strategies = new ArrayList<>();

        @Override
        public StubberBuilder include(Stubber stubber) {
            requireNonNull(stubber, "stubber");
            stubbers.add(0, stubber);
            return this;
        }

        @Override
        public StubberBuilder stubWith(StubbingStrategy strategy) {
            requireNonNull(strategy, "strategy");
            strategies.add(0, strategy);
            return this;
        }

        @Override
        public StubberBuilder stubWith(StubbingStrategy strategy, Matcher<? super Type> matcher) {
            requireNonNull(strategy, "strategy");
            requireNonNull(matcher, "matcher");
            return stubWith(conditional(strategy, matcher));
        }

        @Override
        public StubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies) {
            requireNonNull(strategies, "strategies");
            strategies.forEach(this::stubWith);
            return this;
        }

        @Override
        public StubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies, Matcher<? super Type> matcher) {
            requireNonNull(strategies, "strategies");
            requireNonNull(matcher, "matcher");
            strategies.forEach(strategy -> stubWith(strategy, matcher));
            return this;
        }

        @Override
        public StubberBuilder stubWith(StubbingStrategy... strategies) {
            requireNonNull(strategies, "strategies");
            stubWith(asList(strategies));
            return this;
        }

        @Override
        public Stubber build() {
            Stubber builtStubber = new StubberImpl(strategies);
            if (stubbers.isEmpty()) {
                return builtStubber;
            } else {
                List<Stubber> stubberComposition = new ArrayList<>(this.stubbers);
                stubberComposition.add(0, builtStubber);
                return compose(stubberComposition);
            }
        }

    }

}
