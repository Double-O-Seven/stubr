package ch.leadrian.stubr.core.type;

import ch.leadrian.stubr.core.ParameterizedTypeLiteral;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TypesTest {

    @Nested
    class GetRawTypeTest {

        @Test
        void givenClassItShouldReturnIt() {
            Type type = String.class;
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .hasValue(String.class);
        }

        @Test
        void givenParameterizedTypeItShouldReturnRawType() {
            Type type = new TypeLiteral<List<String>>() {
            }.getType();
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .hasValue(List.class);
        }

        @Test
        void givenWildcardTypeWithLowerBoundItShouldReturnLowerBound() {
            Type type = new ParameterizedTypeLiteral<List<? super Number>>() {
            }.getActualTypeArgument(0);
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithUpperBoundItShouldReturnUpperBound() {
            Type type = new ParameterizedTypeLiteral<List<? extends Number>>() {
            }.getActualTypeArgument(0);
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithoutExplicitBoundItShouldReturnObject() {
            Type type = new ParameterizedTypeLiteral<List<?>>() {
            }.getActualTypeArgument(0);
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .hasValue(Object.class);
        }

        @Test
        <T> void givenTypeVariableItShouldReturnEmpty() {
            Type type = new TypeLiteral<T>() {
            }.getType();
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .isEmpty();
        }

        @Test
        <T> void givenGenericArrayItShouldReturnEmpty() {
            Type type = new TypeLiteral<T[]>() {
            }.getType();
            Optional<Class<?>> clazz = Types.getRawType(type);

            assertThat(clazz)
                    .isEmpty();
        }
    }

    @Nested
    class GetLowerBoundTest {

        @Test
        void shouldReturnLowerBound() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? super Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenOnlyUpperBoundItShouldReturnEmpty() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? extends Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnEmpty() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<?>>() {
            }.getActualTypeArgument(0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

    }

    @Nested
    class GetOnlyUpperBoundTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnObject() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? super Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

        @Test
        void shouldReturnUpperBound() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? extends Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<?>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

    @Nested
    class GetExplicitBound {

        @Test
        void givenOnlyLowerBoundItShouldReturnLowerBound() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? super Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getExplicitBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void shouldReturnUpperBound() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<? extends Number>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getExplicitBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            WildcardType type = (WildcardType) new ParameterizedTypeLiteral<List<?>>() {
            }.getActualTypeArgument(0);

            Optional<Type> upperBound = Types.getExplicitBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

}