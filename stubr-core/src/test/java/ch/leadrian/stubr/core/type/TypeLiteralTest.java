package ch.leadrian.stubr.core.type;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("UnstableApiUsage")
class TypeLiteralTest {

    @Test
    void shouldReturnClass() {
        Type type = new TypeLiteral<String>() {}.getType();

        assertThat(type)
                .isEqualTo(String.class);
    }

    @Test
    void shouldReturnParameterizedType() {
        Type type = new TypeLiteral<List<String>>() {}.getType();

        assertThat(type)
                .isInstanceOfSatisfying(ParameterizedType.class, parameterizedType ->
                        assertAll(
                                () -> assertThat(parameterizedType.getRawType()).isEqualTo(List.class),
                                () -> assertThat(parameterizedType.getActualTypeArguments()[0]).isEqualTo(String.class)
                        )
                );
    }

    @Test
    <T> void testEquals() {
        new EqualsTester()
                .addEqualityGroup(
                        new TypeLiteral<String>() {},
                        new TypeLiteral<String>() {})
                .addEqualityGroup(
                        new TypeLiteral<Integer>() {},
                        new TypeLiteral<Integer>() {})
                .addEqualityGroup(
                        new TypeLiteral<List<String>>() {},
                        new TypeLiteral<List<String>>() {})
                .addEqualityGroup(
                        new TypeLiteral<T>() {},
                        new TypeLiteral<T>() {})
                .testEquals();
    }

    @Test
    void shouldReturnStringRepresentation() {
        String string = new TypeLiteral<String>() {}.toString();

        assertThat(string)
                .isEqualTo("TypeLiteral{type=class java.lang.String}");
    }

    @Test
    void givenSuperclassIsNotParameterizedItShouldThrowException() {
        Throwable caughtThrowable = catchThrowable(() -> new UnparameterizedTypeLiteral() {});

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Superclass must be parameterized");
    }

    @Test
    void givenSuperclassHasMultipleTypeArgumentsItShouldThrowException() {
        Throwable caughtThrowable = catchThrowable(() -> new MultipleParametersTypeLiteral<String, Integer>() {
        });

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Expected exactly one type argument");
    }

    private static abstract class UnparameterizedTypeLiteral extends TypeLiteral<Object> {
    }

    @SuppressWarnings("unused")
    private static abstract class MultipleParametersTypeLiteral<T, U> extends TypeLiteral<T> {
    }

}