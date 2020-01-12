package ch.leadrian.stubr.core.util;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Optional;

import static ch.leadrian.stubr.core.TypeTokens.getTypeArgument;
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
            TypeToken<List<? super Number>> token = new TypeToken<List<? super Number>>() {
            };
            Type type = getTypeArgument(token, 0);
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithUpperBoundItShouldReturnUpperBound() {
            TypeToken<List<? extends Number>> token = new TypeToken<List<? extends Number>>() {
            };
            Type type = getTypeArgument(token, 0);
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Number.class);
        }

        @Test
        void givenWildcardTypeWithoutExplicitBoundItShouldReturnObject() {
            TypeToken<List<?>> token = new TypeToken<List<?>>() {
            };
            Type type = getTypeArgument(token, 0);
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .hasValue(Object.class);
        }

        @Test
        <T> void givenTypeVariableItShouldReturnEmpty() {
            TypeToken<List<T>> token = new TypeToken<List<T>>() {
            };
            Type type = getTypeArgument(token, 0);
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .isEmpty();
        }

        @Test
        <T> void givenGenericArrayItShouldReturnEmpty() {
            TypeToken<List<T[]>> token = new TypeToken<List<T[]>>() {
            };
            Type type = getTypeArgument(token, 0);
            Optional<Class<?>> clazz = Types.getActualClass(type);

            assertThat(clazz)
                    .isEmpty();
        }
    }

    @Nested
    class GetLowerBoundTest {

        @Test
        void shouldReturnLowerBound() {
            TypeToken<List<? super Number>> token = new TypeToken<List<? super Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenOnlyUpperBoundItShouldReturnEmpty() {
            TypeToken<List<? extends Number>> token = new TypeToken<List<? extends Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnEmpty() {
            TypeToken<List<?>> token = new TypeToken<List<?>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> lowerBound = Types.getLowerBound(type);

            assertThat(lowerBound)
                    .isEmpty();
        }

    }

    @Nested
    class GetOnlyUpperBoundTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnObject() {
            TypeToken<List<? super Number>> token = new TypeToken<List<? super Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

        @Test
        void shouldReturnUpperBound() {
            TypeToken<List<? extends Number>> token = new TypeToken<List<? extends Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            TypeToken<List<?>> token = new TypeToken<List<?>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getOnlyUpperBound(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

    @Nested
    class GetMostSpecificTypeTest {

        @Test
        void givenOnlyLowerBoundItShouldReturnLowerBound() {
            TypeToken<List<? super Number>> token = new TypeToken<List<? super Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void shouldReturnUpperBound() {
            TypeToken<List<? extends Number>> token = new TypeToken<List<? extends Number>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Number.class);
        }

        @Test
        void givenNoExplicitBoundsItShouldReturnObject() {
            TypeToken<List<?>> token = new TypeToken<List<?>>() {
            };
            WildcardType type = (WildcardType) getTypeArgument(token, 0);

            Optional<Type> upperBound = Types.getMostSpecificType(type);

            assertThat(upperBound)
                    .hasValue(Object.class);
        }

    }

}