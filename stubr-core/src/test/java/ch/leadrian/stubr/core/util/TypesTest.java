package ch.leadrian.stubr.core.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TypesTest {

    @Nested
    class GetLowerBoundTest {

        @Test
        void shouldReturnLowerBound() {
            WildcardType type = getWildcardType("returnTypeWithLowerBound");

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenOnlyUpperBoundItShouldReturnEmpty() {
            WildcardType type = getWildcardType("returnTypeWithUpperBound");

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnEmpty() {
            WildcardType type = getWildcardType("returnTypeWithoutExplicitBound");

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

    }

    @Nested
    class GetOnlyUpperBoundTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnObject() {
            WildcardType type = getWildcardType("returnTypeWithLowerBound");

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

        @Test
        void shouldReturnUpperBound() {
            WildcardType type = getWildcardType("returnTypeWithUpperBound");

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            WildcardType type = getWildcardType("returnTypeWithoutExplicitBound");

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

    private WildcardType getWildcardType(String methodName) {
        try {
            ParameterizedType returnType = (ParameterizedType) getClass().getDeclaredMethod(methodName).getGenericReturnType();
            return (WildcardType) returnType.getActualTypeArguments()[0];
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unused")
    List<? super Number> returnTypeWithLowerBound() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    List<? extends Number> returnTypeWithUpperBound() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    List<?> returnTypeWithoutExplicitBound() {
        throw new UnsupportedOperationException();
    }

}