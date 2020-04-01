package ch.leadrian.stubr.core.strategy;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static com.google.common.collect.MoreCollectors.toOptional;

class EnumValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testDefaultEnumValueStubber() {
        return stubbingStrategyTester()
                .accepts(Foo.class)
                .andStubs(Foo.FOO)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(StubbingStrategies.enumValue());
    }

    @TestFactory
    Stream<DynamicTest> testSelectiveEnumValueStubber() {
        return stubbingStrategyTester()
                .accepts(Foo.class)
                .andStubs(Foo.FU)
                .accepts(Fubar.class)
                .andStubs(Fubar.FU)
                .rejects(Bla.class)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(StubbingStrategies.enumValue(((context, values) -> values.stream()
                        .filter(value -> "FU".equals(value.name()))
                        .collect(toOptional()))));
    }

    private enum Foo {
        FOO,
        FU
    }

    private enum Fubar {
        FU,
        BAR
    }

    private enum Bar {}

    private enum Bla {
        BLA,
        BLUB
    }

    private static class Qux {}

}