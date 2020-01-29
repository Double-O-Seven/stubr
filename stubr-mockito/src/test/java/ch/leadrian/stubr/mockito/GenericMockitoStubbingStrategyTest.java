package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GenericMockitoStubbingStrategyTest {

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testFinalStubbingGenericMockitoStubber() {
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
                .accepts(new TypeLiteral<Bla<String>>() {})
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Bla.class, bla ->
                        assertAll(
                                () -> assertThat(bla.getInt()).isEqualTo(1337),
                                () -> assertThat(bla.getString()).isEqualTo("Test")
                        )
                ))
                .accepts(Bar.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Bar.class, bar ->
                        assertAll(
                                () -> assertThat(bar.getInt()).isEqualTo(1337),
                                () -> assertThat(bar.getString()).isEqualTo("Test")
                        )
                ))
                .accepts(FinalClass.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(FinalClass.class, finalClass ->
                        assertAll(
                                () -> assertThat(finalClass.getInt()).isEqualTo(1337),
                                () -> assertThat(finalClass.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(int.class)
                .rejects(Object[].class)
                .rejects(Qux.class)
                .test(MockitoStubbers.mock(true));
    }

    @TestFactory
    Stream<DynamicTest> testOpenOnlyStubbingGenericMockitoStubber() {
        return stubbingStrategyTester()
                .rejects(int.class)
                .rejects(FinalClass.class)
                .rejects(Object[].class)
                .rejects(Qux.class)
                .test(
                        MockitoStubbers.mock(false),
                        MockitoStubbers.mock()
                );
    }

    interface Foo {

        int getInt();

        String getString();

    }

    interface Bla<T> {

        int getInt();

        String getString();

    }

    class Bar {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

    final class FinalClass {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

    enum Qux {}

}