package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.testing.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static java.util.Collections.singletonList;

class ConstantValueStubberTest {

    @TestFactory
    Stream<DynamicTest> testConstantValueStubber() {
        return stubberTester()
                .accepts(BigDecimal.class)
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? extends BigDecimal>>() {
                }.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? super BigDecimal>>() {
                }.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .rejects(Number.class)
                .rejects(new BigDecimal(1337) {
                }.getClass())
                .test(
                        Stubbers.constantValue(new BigDecimal(1337)),
                        Stubbers.constantValue(BigDecimal.class, new BigDecimal(1337)),
                        Stubbers.constantValue(new TypeLiteral<BigDecimal>() {
                        }, new BigDecimal(1337))
                );
    }

    @TestFactory
    Stream<DynamicTest> testConstantValueStubberWithParameterizedType() {
        TypeLiteral<List<String>> listOfStrings = new TypeLiteral<List<String>>() {
        };
        return stubberTester()
                .accepts(listOfStrings)
                .andStubs(singletonList("Test"))
                .rejects(new TypeLiteral<List<Integer>>() {
                })
                .test(Stubbers.constantValue(listOfStrings, singletonList("Test")));
    }

}