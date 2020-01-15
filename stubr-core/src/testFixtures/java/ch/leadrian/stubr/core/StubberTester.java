package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class StubberTester {

    private final List<StubberTest> tests = new ArrayList<>();
    private final Map<Type, ResultProvider> resultProvidersByType = new HashMap<>();

    private StubberTester addResultProvider(Type type, ResultProvider resultProvider) {
        if (resultProvidersByType.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Value for %s is already provided", type));
        }
        resultProvidersByType.put(type, resultProvider);
        return this;
    }

    public StubberTester provideStub(Type type, Object... values) {
        return addResultProvider(type, ResultProvider.of(values));
    }

    public StubberTester provideStub(Object value) {
        return provideStub(value.getClass(), value);
    }

    @SafeVarargs
    public final <T> StubberTester provideStub(Class<T> type, T... values) {
        return provideStub(type, (Object[]) values);
    }

    @SafeVarargs
    public final <T> StubberTester provideStub(TypeLiteral<T> typeLiteral, T... values) {
        return provideStub(typeLiteral.getType(), (Object[]) values);
    }

    public StubberTester doNotStub(Type type) {
        return addResultProvider(type, ResultProvider.of());
    }

    public StubberTester doNotStub(TypeLiteral<?> typeLiteral) {
        return doNotStub(typeLiteral.getType());
    }

    public StubberTester accepts(Type type, Object expectedValue) {
        tests.add(new StubberAcceptsType(type));
        tests.add(new StubberProvidesStub(type, expectedValue));
        return this;
    }

    public <T> StubberTester accepts(Class<T> type, T expectedValue) {
        return accepts((Type) type, expectedValue);
    }

    public <T> StubberTester accepts(TypeLiteral<T> typeLiteral, T expectedValue) {
        return accepts(typeLiteral.getType(), expectedValue);
    }

    public StubberTester reject(Type type) {
        tests.add(new StubberRejectsType(type));
        return this;
    }

    public StubberTester reject(TypeLiteral<?> typeLiteral) {
        return reject(typeLiteral.getType());
    }

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

}
