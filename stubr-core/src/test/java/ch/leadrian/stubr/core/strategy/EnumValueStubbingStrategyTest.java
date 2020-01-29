package ch.leadrian.stubr.core.strategy;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;

class EnumValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testEnumValueStubberTest() {
        return stubbingStrategyTester()
                .accepts(Foo.class)
                .andStubs(Foo.FOO)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(StubbingStrategies.enumValue());
    }

    private enum Foo {
        FOO,
        FU
    }

    private enum Bar {}

    private class Qux {
    }

}