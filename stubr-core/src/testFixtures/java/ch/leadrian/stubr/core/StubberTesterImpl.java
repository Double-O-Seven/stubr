package ch.leadrian.stubr.core;

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

final class StubberTesterImpl implements StubberTester {

    private final List<StubberTest> tests = new ArrayList<>();
    private final Map<Type, ResultProvider> resultProvidersByType = new HashMap<>();

    private StubberTester addResultProvider(Type type, ResultProvider resultProvider) {
        if (resultProvidersByType.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Value for %s is already provided", type));
        }
        resultProvidersByType.put(type, resultProvider);
        return this;
    }

    @Override
    public StubberTester provideStub(Type type, Object... values) {
        return addResultProvider(type, ResultProvider.of(values));
    }

    @Override
    public StubberTester doNotStub(Type type) {
        return addResultProvider(type, ResultProvider.of());
    }

    @Override
    public <T> AndStubsStep<T> accepts(Type type) {
        tests.add(new StubberAcceptsType(type));
        return new AndStubStepImpl<>(type);
    }

    @Override
    public StubberTester rejects(Type type) {
        tests.add(new StubberRejectsType(type));
        return this;
    }

    @Override
    public Stream<DynamicTest> test(Stubber stubber) {
        return new ArrayList<>(tests)
                .stream()
                .map(test -> {
                    RootStubber rootStubber = createRootStubber();
                    StubbingContext context = new StubbingContext(rootStubber, TestStubbingSite.INSTANCE);
                    return test.toDynamicTest(stubber, context);
                });
    }

    private RootStubber createRootStubber() {
        Map<Type, ResultProvider> untouchedResultProvidersByType = new HashMap<>(resultProvidersByType);
        untouchedResultProvidersByType.replaceAll((type, resultProvider) -> resultProvider.getUntouchedInstance());
        return new TestRootStubber(untouchedResultProvidersByType);
    }

    private abstract class DelegatingStubberTester implements StubberTester {

        @Override
        public StubberTester provideStub(Type type, Object... values) {
            return StubberTesterImpl.this.provideStub(type, values);
        }

        @Override
        public StubberTester doNotStub(Type type) {
            return StubberTesterImpl.this.doNotStub(type);
        }

        @Override
        public <T> AndStubsStep<T> accepts(Type type) {
            return StubberTesterImpl.this.accepts(type);
        }

        @Override
        public StubberTester rejects(Type type) {
            return StubberTesterImpl.this.rejects(type);
        }

        @Override
        public Stream<DynamicTest> test(Stubber stubber) {
            return StubberTesterImpl.this.test(stubber);
        }
    }

    private final class AndStubStepImpl<T> extends DelegatingStubberTester implements AndStubsStep<T> {

        private final Type type;

        private AndStubStepImpl(Type type) {
            this.type = type;
        }

        @Override
        public AtSiteStep andStubs(T expectedValue) {
            tests.add(new StubberProvidesStub(type, expectedValue));
            return new AtSiteStepImpl(type);
        }
    }

    private final class AtSiteStepImpl extends DelegatingStubberTester implements AtSiteStep {

        private final Type type;

        private AtSiteStepImpl(Type type) {
            this.type = type;
        }

        @Override
        public StubberTester atSite(StubbingSite... expectedSites) {
            tests.add(new StubberStubsAtSite(type, asList(expectedSites)));
            return StubberTesterImpl.this;
        }
    }

}
