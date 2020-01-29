package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.testing.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;

class ConstantValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testConstantValueStubber() {
        return stubbingStrategyTester()
                .accepts(BigDecimal.class)
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? extends BigDecimal>>() {}.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? super BigDecimal>>() {}.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .rejects(Number.class)
                .rejects(new BigDecimal(1337) {}.getClass())
                .test(
                        StubbingStrategies.constantValue(new BigDecimal(1337)),
                        StubbingStrategies.constantValue(BigDecimal.class, new BigDecimal(1337)),
                        StubbingStrategies.constantValue(new TypeLiteral<BigDecimal>() {}, new BigDecimal(1337))
                );
    }

    @TestFactory
    Stream<DynamicTest> testConstantValueStubberWithParameterizedType() {
        TypeLiteral<List<String>> listOfStrings = new TypeLiteral<List<String>>() {};
        return stubbingStrategyTester()
                .accepts(listOfStrings)
                .andStubs(singletonList("Test"))
                .rejects(new TypeLiteral<List<Integer>>() {})
                .test(StubbingStrategies.constantValue(listOfStrings, singletonList("Test")));
    }

}