package ch.leadrian.stubr.core.util;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("UnstableApiUsage")
class TypesTest {

    @Nested
    class GetActualCassTest {

        @Test
        void givenClassItShouldReturnIt() {
            Type type = new TypeToken<String>() {
            }.getType();
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(String.class);
        }

        @Test
        void givenParameterizedTypeItShouldReturnRawType() {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(List.class);
        }

        @Test
        void givenWildcardTypeWithLowerBoundItShouldReturnLowerBound() {
            Type type = ((ParameterizedType) new TypeToken<List<? super Number>>() {
            }.getType()).getActualTypeArguments()[0];
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithUpperBoundItShouldReturnUpperBound() {
            Type type = ((ParameterizedType) new TypeToken<List<? extends Number>>() {
            }.getType()).getActualTypeArguments()[0];
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithoutExplicitBoundItShouldReturnObject() {
            Type type = ((ParameterizedType) new TypeToken<List<?>>() {
            }.getType()).getActualTypeArguments()[0];
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Object.class);
        }

        @Test
        <T> void givenTypeVariableItShouldReturnEmpty() {
            Type type = ((ParameterizedType) new TypeToken<List<T>>() {
            }.getType()).getActualTypeArguments()[0];
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .isEmpty();
        }

        @Test
        <T> void givenGenericArrayItShouldReturnEmpty() {
            Type type = ((ParameterizedType) new TypeToken<List<T[]>>() {
            }.getType()).getActualTypeArguments()[0];
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .isEmpty();
        }
    }

    @Nested
    class GetLowerBoundTest {

        @Test
        void shouldReturnLowerBound() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? super Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenOnlyUpperBoundItShouldReturnEmpty() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? extends Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnEmpty() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<?>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

    }

    @Nested
    class GetOnlyUpperBoundTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnObject() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? super Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

        @Test
        void shouldReturnUpperBound() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? extends Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<?>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

    @Nested
    class GetMostSpecificTypeTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnLowerBound() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? super Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void shouldReturnUpperBound() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<? extends Number>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            WildcardType type = (WildcardType) ((ParameterizedType) new TypeToken<List<?>>() {
            }.getType()).getActualTypeArguments()[0];

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

}