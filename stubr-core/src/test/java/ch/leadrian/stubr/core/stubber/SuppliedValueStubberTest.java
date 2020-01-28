package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SuppliedValueStubberTest {

    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValue() {
        return stubberTester()
                .accepts(String.class)
                .andStubs("Test")
                .rejects(Object.class)
                .test(
                        Stubbers.suppliedValue(String.class, () -> "Test"),
                        Stubbers.suppliedValue(String.class, sequenceNumber -> "Test")
                );
    }

    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValueOfParameterizedType() {
        TypeLiteral<List<String>> listOfStrings = new TypeLiteral<List<String>>() {};
        return stubberTester()
                .accepts(listOfStrings)
                .andStubs(singletonList("Test"))
                .rejects(new TypeLiteral<List<Integer>>() {})
                .test(
                        Stubbers.suppliedValue(listOfStrings, () -> singletonList("Test")),
                        Stubbers.suppliedValue(listOfStrings, sequenceNumber -> singletonList("Test"))
                );
    }

    @Test
    void shouldSuppliedSequencedValue() {
        Stubber stubber = Stubbers.suppliedValue(Integer.class, sequenceNumber -> sequenceNumber);

        List<Object> values = IntStream.generate(() -> 0)
                .limit(3)
                .mapToObj(i -> stubber.stub(mock(StubbingContext.class), Integer.class))
                .collect(toList());

        assertThat(values)
                .containsExactly(0, 1, 2);
    }

}