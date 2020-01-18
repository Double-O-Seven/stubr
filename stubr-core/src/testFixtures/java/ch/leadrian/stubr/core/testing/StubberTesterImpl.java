package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
    public <T> StubValueTester<T> accepts(Type type) {
        tests.add(new StubberAcceptsType(type));
        return new StubValueTesterImpl<>(type);
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
        public <T> StubValueTester<T> accepts(Type type) {
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

    private final class StubValueTesterImpl<T> extends DelegatingStubberTester implements StubValueTester<T> {

        private final Type type;

        private StubValueTesterImpl(Type type) {
            this.type = type;
        }

        @Override
        public SiteTester andStubs(T expectedValue) {
            tests.add(new StubberProvidesStub(type, expectedValue));
            return new SiteTesterImpl(type);
        }

        @Override
        public SiteTester andStubSatisfies(Consumer<Object> assertion) {
            tests.add(new StubberProvidesStubSatisfying(type, assertion));
            return new SiteTesterImpl(type);
        }
    }

    private final class SiteTesterImpl extends DelegatingStubberTester implements SiteTester {

        private final Type type;

        private SiteTesterImpl(Type type) {
            this.type = type;
        }

        @Override
        public StubberTester at(StubbingSite... expectedSites) {
            tests.add(new StubberStubsAtSite(type, asList(expectedSites)));
            return StubberTesterImpl.this;
        }
    }

}
