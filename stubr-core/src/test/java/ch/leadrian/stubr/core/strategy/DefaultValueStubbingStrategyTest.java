package ch.leadrian.stubr.core.strategy;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;

class DefaultValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testDefaultValueStubber() {
        return stubbingStrategyTester()
                .accepts(byte.class)
                .andStubs((byte) 0)
                .accepts(Byte.class)
                .andStubs((byte) 0)
                .accepts(short.class)
                .andStubs((short) 0)
                .accepts(Short.class)
                .andStubs((short) 0)
                .accepts(char.class)
                .andStubs('\0')
                .accepts(Character.class)
                .andStubs('\0')
                .accepts(int.class)
                .andStubs(0)
                .accepts(Integer.class)
                .andStubs(0)
                .accepts(long.class)
                .andStubs(0L)
                .accepts(Long.class)
                .andStubs(0L)
                .accepts(float.class)
                .andStubs(0f)
                .accepts(Float.class)
                .andStubs(0f)
                .accepts(double.class)
                .andStubs(0.0)
                .accepts(Double.class)
                .andStubs(0.0)
                .accepts(boolean.class)
                .andStubs(false)
                .accepts(Boolean.class)
                .andStubs(false)
                .rejects(String.class)
                .rejects(Object.class)
                .test(StubbingStrategies.defaultValue());
    }

}