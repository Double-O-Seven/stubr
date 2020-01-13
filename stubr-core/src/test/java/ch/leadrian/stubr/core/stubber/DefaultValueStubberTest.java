package ch.leadrian.stubr.core.stubber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class DefaultValueStubberTest {

    @ParameterizedTest
    @ArgumentsSource(AcceptedTypesArgumentsProvider.class)
    void shouldReturnDefaultValueForByte(Class<?> clazz) {
        boolean accepted = DefaultValueStubber.INSTANCE.accepts(null, clazz);

        assertThat(accepted)
                .isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(StubbedValuesArgumentsProvider.class)
    void shouldStubValue(Class<?> clazz, Object expectedValue) {
        Object value = DefaultValueStubber.INSTANCE.stub(null, clazz);

        assertThat(value)
                .isEqualTo(expectedValue);
    }

    @Test
    void shouldNotAcceptUnknownClass() {
        boolean accepted = DefaultValueStubber.INSTANCE.accepts(null, Foo.class);

        assertThat(accepted)
                .isFalse();
    }

    @Test
    void givenUnknownClassStubShouldThrowException() {
        Throwable caughtThrowable = catchThrowable(() -> DefaultValueStubber.INSTANCE.stub(null, Foo.class));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    private static final class AcceptedTypesArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    byte.class,
                    Byte.class,
                    short.class,
                    Short.class,
                    char.class,
                    Character.class,
                    int.class,
                    Integer.class,
                    long.class,
                    Long.class,
                    float.class,
                    Float.class,
                    double.class,
                    Double.class)
                    .map(Arguments::of);
        }
    }

    private static final class StubbedValuesArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(byte.class, (byte) 0),
                    Arguments.of(Byte.class, (byte) 0),
                    Arguments.of(short.class, (short) 0),
                    Arguments.of(Short.class, (short) 0),
                    Arguments.of(char.class, '\0'),
                    Arguments.of(Character.class, '\0'),
                    Arguments.of(int.class, 0),
                    Arguments.of(Integer.class, 0),
                    Arguments.of(long.class, 0L),
                    Arguments.of(Long.class, 0L),
                    Arguments.of(float.class, 0f),
                    Arguments.of(Float.class, 0f),
                    Arguments.of(double.class, 0.0),
                    Arguments.of(Double.class, 0.0));
        }
    }

    private static final class Foo {
    }
}