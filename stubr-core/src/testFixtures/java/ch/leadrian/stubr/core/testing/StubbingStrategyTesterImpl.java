package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.StubbingStrategy;
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

    private final List<StubbingStrategyTest> tests = new ArrayList<>();
    private final Map<Type, ResultProvider> resultProvidersByType = new HashMap<>();

    private StubbingStrategyTester addResultProvider(Type type, ResultProvider resultProvider) {
        if (resultProvidersByType.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Value for %s is already provided", type));
        }
        resultProvidersByType.put(type, resultProvider);
        return this;
    }

    @Override
    public StubbingStrategyTester provideStub(Type type, Object... values) {
        return addResultProvider(type, ResultProvider.of(values));
    }

    @Override
    public StubbingStrategyTester doNotStub(Type type) {
        return addResultProvider(type, ResultProvider.of());
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
                    Stubber stubber = createStubber();
                    StubbingContext context = StubbingContext.create(stubber, TestStubbingSite.INSTANCE);
                    return test.toDynamicTest(stubbingStrategy, context);
                });
    }

    private Stubber createStubber() {
        Map<Type, ResultProvider> untouchedResultProvidersByType = new HashMap<>(resultProvidersByType);
        untouchedResultProvidersByType.replaceAll((type, resultProvider) -> resultProvider.getUntouchedInstance());
        return new TestStubber(untouchedResultProvidersByType);
    }

    private abstract class DelegatingStubbingStrategyTester implements StubbingStrategyTester {

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
