package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface StubberTester {

    static StubberTester stubberTester() {
        return new StubberTesterImpl();
    }

    StubberTester provideStub(Type type, Object... values);

    default StubberTester provideStub(Object value) {
        return provideStub(value.getClass(), value);
    }

    @SuppressWarnings("unchecked")
    default <T> StubberTester provideStub(Class<T> type, T... values) {
        return provideStub(type, (Object[]) values);
    }

    @SuppressWarnings("unchecked")
    default <T> StubberTester provideStub(TypeLiteral<T> typeLiteral, T... values) {
        return provideStub(typeLiteral.getType(), (Object[]) values);
    }

    StubberTester doNotStub(Type type);

    default StubberTester doNotStub(TypeLiteral<?> typeLiteral) {
        return doNotStub(typeLiteral.getType());
    }

    <T> AndStubsStep<T> accepts(Type type);

    default <T> AndStubsStep<T> accepts(Class<T> type) {
        return accepts((Type) type);
    }

    default <T> AndStubsStep<T> accepts(TypeLiteral<T> typeLiteral) {
        return accepts(typeLiteral.getType());
    }

    StubberTester rejects(Type type);

    default StubberTester rejects(TypeLiteral<?> typeLiteral) {
        return rejects(typeLiteral.getType());
    }

    Stream<DynamicTest> test(Stubber stubber);

    interface AndStubsStep<T> extends StubberTester {

        AtSiteStep andStubs(T expectedValue);

    }

    interface AtSiteStep extends StubberTester {

        StubberTester atSite(StubbingSite... expectedSites);

    }
}
