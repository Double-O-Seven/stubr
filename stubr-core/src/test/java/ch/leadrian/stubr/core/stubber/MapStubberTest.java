package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static ch.leadrian.stubr.core.TypeTokens.getTypeArgument;
import static ch.leadrian.stubr.core.util.Types.getRawType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
class MapStubberTest {

    private StubbingContext context;
    private RootStubber rootStubber;

    @BeforeEach
    void setUp() {
        rootStubber = mock(RootStubber.class);
        when(rootStubber.stub(any(Type.class), any()))
                .thenAnswer(invocation -> {
                    Class<?> rawType = getRawType(invocation.getArgument(0, Type.class)).orElseThrow(UnsupportedOperationException::new);
                    if (rawType == String.class) {
                        return "Test";
                    } else if (rawType == Integer.class) {
                        return 1234;
                    } else if (rawType == Number.class) {
                        return 1337;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });
        context = new StubbingContext(rootStubber, StubbingSites.unknown());
    }

    @Test
    void givenMapSizeOfZeroItShouldAcceptNotParameterizedMap() {
        Type type = Map.class;
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenMapSizeOfZeroItShouldStubNotParameterizedMap() {
        Type type = Map.class;
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(HashMap.class, map -> assertThat(map).isEmpty());
    }

    @Test
    void givenMapSizeGreaterThanZeroItShouldNotAcceptNotParameterizedMap() {
        Type type = Map.class;
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenMapSizeOfZeroItShouldAcceptParameterizedMap() {
        Type type = new TypeToken<Map<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenMapSizeOfZeroItShouldStubParameterizedMap() {
        Type type = new TypeToken<Map<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(HashMap.class, map -> assertThat(map).isEmpty());
    }

    @Test
    void givenMapSizeGreaterThanZeroItShouldAcceptParameterizedMap() {
        Type type = new TypeToken<Map<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenMapSizeGreaterThanZeroItShouldStubParameterizedMap() {
        Type type = new TypeToken<Map<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(HashMap.class, map -> assertThat(map)
                        .containsExactly(entry(1234, "Test")));
    }

    @Test
    void shouldAcceptMapWithUpperBoundedWildcardType() {
        Type type = new TypeToken<Map<? extends Number, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubMapWithUpperBoundedWildcardType() {
        Type type = new TypeToken<Map<? extends Number, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(HashMap.class, map -> assertThat(map)
                        .containsExactly(entry(1337, "Test")));
    }

    @Test
    void shouldAcceptMapWithLowerBoundedWildcardType() {
        Type type = new TypeToken<Map<? super Number, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubMapWithLowerBoundedWildcardType() {
        Type type = new TypeToken<Map<? super Number, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(HashMap.class, map -> assertThat(map)
                        .containsExactly(entry(1337, "Test")));
    }

    @Test
    <T, U> void shouldAcceptMapWithTypeVariable() {
        Type type = new TypeToken<Map<T, U>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    <T, U> void shouldNotAcceptTypeVariable() {
        TypeToken<Map<T, U>> token = new TypeToken<Map<T, U>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    <T> void shouldThrowExceptionWhenStubbingTypeVariable() {
        TypeToken<Map<T, String>> token = new TypeToken<Map<T, String>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    <T, U> void shouldNotAcceptGenericArrayType() {
        TypeToken<Map<T[], U>> token = new TypeToken<Map<T[], U>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldNotAcceptMapThatIsNoExactMatch() {
        Type type = HashMap.class;
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptParameterizedMapThatIsNoExactMatch() {
        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptNonMapType() {
        Type type = Number.class;
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldUseParameterizedTypeStubbingSite() {
        Type type = new TypeToken<Map<Integer, String>>() {
        }.getType();
        Stubber stubber = new MapStubber<>(Map.class, HashMap::new, context -> 1);

        stubber.stub(context, type);

        verify(rootStubber, times(2)).stub(any(Type.class), any());
        verify(rootStubber).stub((Type) Integer.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 0));
        verify(rootStubber).stub((Type) String.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 1));
    }

}