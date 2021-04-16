/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.internal.com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.strategy.StubbingStrategies.conditional;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

final class DefaultStubber extends Stubber {

    private final List<StubbingStrategy> strategies;

    private DefaultStubber(List<StubbingStrategy> strategies) {
        this.strategies = ImmutableList.copyOf(strategies);
    }

    @Override
    Stream<StubbingContext> newContextStream(Stubber rootStubber, StubbingSite site, Type type) {
        return strategies.stream().map(strategy -> new StubbingContext(rootStubber, site, type, strategy));
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
            Stubber builtStubber = new DefaultStubber(strategies);
            if (stubbers.isEmpty()) {
                return builtStubber;
            } else {
                List<Stubber> stubberComposition = new ArrayList<>(this.stubbers);
                stubberComposition.add(0, builtStubber);
                return new CompositeStubber(stubberComposition);
            }
        }

    }

}
