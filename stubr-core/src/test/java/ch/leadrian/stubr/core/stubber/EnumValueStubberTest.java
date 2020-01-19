package ch.leadrian.stubr.core.stubber;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;

class EnumValueStubberTest {

    @TestFactory
    Stream<DynamicTest> testEnumValueStubberTest() {
        return stubberTester()
                .accepts(Foo.class)
                .andStubs(Foo.FOO)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(Stubbers.enumValue());
    }

    private enum Foo {
        FOO,
        FU
    }

    private enum Bar {}

    private class Qux {
    }

}