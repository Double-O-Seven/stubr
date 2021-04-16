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

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

final class StubbingStrategyTesterImpl implements StubbingStrategyTester {

    private final List<StubbingStrategyTestCase> tests = new ArrayList<>();
    private final Map<Type, ValueProvider> resultProvidersByType = new HashMap<>();
    private final List<Stubber> stubbers = new ArrayList<>();

    private StubbingStrategyTester addResultProvider(Type type, ValueProvider valueProvider) {
        if (resultProvidersByType.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Value for %s is already provided", type));
        }
        resultProvidersByType.put(type, valueProvider);
        return this;
    }

    @Override
    public StubbingStrategyTester provideStubsWith(Stubber stubber) {
        stubbers.add(stubber);
        return this;
    }

    @Override
    public StubbingStrategyTester provideStub(Type type, Object... values) {
        return addResultProvider(type, ValueProvider.of(values));
    }

    @Override
    public StubbingStrategyTester doNotStub(Type type) {
        return addResultProvider(type, ValueProvider.of());
    }

    @Override
    public <T> StubValueTester<T> accepts(Type type) {
        tests.add(new StubbingStrategyAcceptsType(type));
        return new StubValueTesterImpl<>(type);
    }

    @Override
    public StubbingStrategyTester rejects(Type type) {
        tests.add(new StubbingStrategyRejectsType(type));
        return this;
    }

    @Override
    public Stream<DynamicTest> test(StubbingStrategy stubbingStrategy) {
        return new ArrayList<>(tests)
                .stream()
                .map(test -> {
                    Stubber stubber = createStubber(stubbingStrategy);
                    return test.toDynamicTest(stubbingStrategy, stubber, TestStubbingSite.INSTANCE);
                });
    }

    private Stubber createStubber(StubbingStrategy stubbingStrategy) {
        Map<Type, ValueProvider> untouchedResultProvidersByType = new HashMap<>(resultProvidersByType);
        untouchedResultProvidersByType.replaceAll((type, valueProvider) -> valueProvider.getUntouchedInstance());
        ProvidedValueStrategy providedValueStrategy = new ProvidedValueStrategy(untouchedResultProvidersByType);
        StubberBuilder builder = Stubber.builder();
        builder.include(Stubber.builder().stubWith(providedValueStrategy).build());
        stubbers.forEach(builder::include);
        return builder
                .stubWith(stubbingStrategy)
                .build();
    }

    private abstract class DelegatingStubbingStrategyTester implements StubbingStrategyTester {

        @Override
        public StubbingStrategyTester provideStubsWith(Stubber stubber) {
            return StubbingStrategyTesterImpl.this.provideStubsWith(stubber);
        }

        @Override
        public StubbingStrategyTester provideStub(Type type, Object... values) {
            return StubbingStrategyTesterImpl.this.provideStub(type, values);
        }

        @Override
        public StubbingStrategyTester doNotStub(Type type) {
            return StubbingStrategyTesterImpl.this.doNotStub(type);
        }

        @Override
        public <T> StubValueTester<T> accepts(Type type) {
            return StubbingStrategyTesterImpl.this.accepts(type);
        }

        @Override
        public StubbingStrategyTester rejects(Type type) {
            return StubbingStrategyTesterImpl.this.rejects(type);
        }

        @Override
        public Stream<DynamicTest> test(StubbingStrategy stubbingStrategy) {
            return StubbingStrategyTesterImpl.this.test(stubbingStrategy);
        }

    }

    private final class StubValueTesterImpl<T> extends DelegatingStubbingStrategyTester implements StubValueTester<T> {

        private final Type type;

        private StubValueTesterImpl(Type type) {
            this.type = type;
        }

        @Override
        public SiteTester andStubs(T expectedValue) {
            tests.add(new StubbingStrategyProvidesStub(type, expectedValue));
            return new SiteTesterImpl(type);
        }

        @Override
        public SiteTester andStubSatisfies(Consumer<Object> assertion) {
            tests.add(new StubbingStrategyProvidesStubSatisfying(type, assertion));
            return new SiteTesterImpl(type);
        }

    }

    private final class SiteTesterImpl extends DelegatingStubbingStrategyTester implements SiteTester {

        private final Type type;

        private SiteTesterImpl(Type type) {
            this.type = type;
        }

        @Override
        public StubbingStrategyTester at(StubbingSite... expectedSites) {
            tests.add(new StubbingStrategyStubsAtSite(type, asList(expectedSites)));
            return StubbingStrategyTesterImpl.this;
        }

    }

}
