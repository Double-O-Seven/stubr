package ch.leadrian.stubr.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TypeLiteralTest {

    @Test
    void shouldReturnClass() {
        Type type = new TypeLiteral<String>() {
        }.getType();

        assertThat(type)
                .isEqualTo(String.class);
    }

    @Test
    void shouldReturnParameterizedType() {
        Type type = new TypeLiteral<List<String>>() {
        }.getType();

        assertThat(type)
                .isInstanceOfSatisfying(ParameterizedType.class, parameterizedType ->
                        assertAll(
                                () -> assertThat(parameterizedType.getRawType()).isEqualTo(List.class),
                                () -> assertThat(parameterizedType.getActualTypeArguments()[0]).isEqualTo(String.class)
                        )
                );
    }

}