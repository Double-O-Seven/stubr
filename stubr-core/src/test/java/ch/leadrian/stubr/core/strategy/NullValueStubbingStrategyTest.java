package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;

class NullValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testNullValueStubber() {
        return stubberTester()
                .accepts(Object.class)
                .andStubs(null)
                .accepts(String.class)
                .andStubs(null)
                .accepts(new TypeLiteral<List<String>>() {})
                .andStubs(null)
                .rejects(boolean.class)
                .rejects(byte.class)
                .rejects(short.class)
                .rejects(char.class)
                .rejects(int.class)
                .rejects(long.class)
                .rejects(float.class)
                .rejects(double.class)
                .test(StubbingStrategies.nullValue());
    }

}