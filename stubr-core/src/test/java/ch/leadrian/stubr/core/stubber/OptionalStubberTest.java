package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;

class OptionalStubberTest {

    @TestFactory
    Stream<DynamicTest> testEmptyOptionalStubber() {
        return stubberTester()
                .accepts(Optional.class)
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<? super String>>() {})
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<? extends String>>() {})
                .andStubs(Optional.empty())
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(Stubbers.optional(OptionalStubbingMode.EMPTY));
    }

    @TestFactory
    Stream<DynamicTest> testPresentOptionalStubber() {
        return stubberTester()
                .provideStub("Test")
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.of("Test"))
                .rejects(Optional.class)
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(Stubbers.optional(OptionalStubbingMode.PRESENT));
    }

    @TestFactory
    Stream<DynamicTest> testPresentIfPossibleOptionalStubber() {
        return stubberTester()
                .provideStub("Test")
                .doNotStub(Integer.class)
                .accepts(Optional.class)
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.of("Test"))
                .accepts(new TypeLiteral<Optional<Integer>>() {})
                .andStubs(Optional.empty())
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(Stubbers.optional(OptionalStubbingMode.PRESENT_IF_POSSIBLE));
    }

}