package ch.leadrian.stubr.core.stubber;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProxyStubberTest {

    @TestFactory
    Stream<DynamicTest> testNonCachingProxyStubber() {
        return stubberTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 2, 3),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(
                        Stubbers.proxy(false),
                        Stubbers.proxy()
                );
    }

    @TestFactory
    Stream<DynamicTest> testCachingProxyStubber() {
        return stubberTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 1, 1),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(Stubbers.proxy(true));
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