package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
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

class SuppliedValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValue() {
        return stubberTester()
                .accepts(String.class)
                .andStubs("Test")
                .rejects(Object.class)
                .test(
                        StubbingStrategies.suppliedValue(String.class, () -> "Test"),
                        StubbingStrategies.suppliedValue(String.class, sequenceNumber -> "Test")
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
                        StubbingStrategies.suppliedValue(listOfStrings, () -> singletonList("Test")),
                        StubbingStrategies.suppliedValue(listOfStrings, sequenceNumber -> singletonList("Test"))
                );
    }

    @Test
    void shouldSuppliedSequencedValue() {
        StubbingStrategy stubbingStrategy = StubbingStrategies.suppliedValue(Integer.class, sequenceNumber -> sequenceNumber);

        List<Object> values = IntStream.generate(() -> 0)
                .limit(3)
                .mapToObj(i -> stubbingStrategy.stub(mock(StubbingContext.class), Integer.class))
                .collect(toList());

        assertThat(values)
                .containsExactly(0, 1, 2);
    }

}