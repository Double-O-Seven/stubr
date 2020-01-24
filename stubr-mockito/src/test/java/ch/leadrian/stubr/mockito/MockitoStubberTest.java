package ch.leadrian.stubr.mockito;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class MockitoStubberTest {

    @TestFactory
    Stream<DynamicTest> testMockitoStubber() {
        return stubberTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo ->
                        assertAll(
                                () -> assertThat(foo.getInt()).isEqualTo(1337),
                                () -> assertThat(foo.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbers.mock(Foo.class));
    }

    @TestFactory
    Stream<DynamicTest> testMockitoStubberWithMockConfiguration() {
        return stubberTester()
                .provideStub(int.class, 1234)
                .provideStub("bla")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo ->
                        assertAll(
                                () -> assertThat(foo.getInt()).isEqualTo(1337),
                                () -> assertThat(foo.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbers.mock(Foo.class, mock -> {
                    when(mock.getInt()).thenReturn(1337);
                    when(mock.getString()).thenReturn("Test");
                }));
    }

    class Foo {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

}