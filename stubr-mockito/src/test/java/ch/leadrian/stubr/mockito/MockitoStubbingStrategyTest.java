package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class MockitoStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testMockitoStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo ->
                        assertAll(
                                () -> assertThat(foo.getInt()).isEqualTo(1337),
                                () -> assertThat(foo.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(new TypeLiteral<Bar<String>>() {})
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbers.mock(Foo.class));
    }

    @TestFactory
    Stream<DynamicTest> testMockitoStubberWithParameterizedType() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(new TypeLiteral<Bar<String>>() {})
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Bar.class, bar ->
                        assertAll(
                                () -> assertThat(bar.getInt()).isEqualTo(1337),
                                () -> assertThat(bar.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(Foo.class)
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbers.mock(Bar.class));
    }

    @TestFactory
    Stream<DynamicTest> testMockitoStubberWithMockConfiguration() {
        return stubbingStrategyTester()
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

    class Bar<T> {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

}