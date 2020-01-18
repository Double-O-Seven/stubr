package ch.leadrian.stubr.core.stubber;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProxyStubberTest {

    @TestFactory
    Stream<DynamicTest> testNonCachingProxyStubber() {
        return stubberTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(foo.getInt()).isEqualTo(1337),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(Stubbers.proxy(false));
    }

    private interface Foo {

        int getInt();

        default int getAnotherInt() {
            return 1234;
        }

        String getString();

    }

    private abstract class Bar {
    }

    private class Qux {
    }

}