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
import java.util.ArrayList;
import java.util.Collection;

import static ch.leadrian.stubr.core.TypeTokens.getTypeArgument;
import static ch.leadrian.stubr.core.util.Types.getActualClass;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
class CollectionStubberTest {

    private StubbingContext context;
    private RootStubber rootStubber;

    @BeforeEach
    void setUp() {
        rootStubber = mock(RootStubber.class);
        when(rootStubber.stub(any(Type.class), any()))
                .thenAnswer(invocation -> {
                    Class<?> actualClass = getActualClass(invocation.getArgument(0, Type.class)).orElseThrow(UnsupportedOperationException::new);
                    if (actualClass == String.class) {
                        return "Test";
                    } else if (actualClass == Number.class) {
                        return 1337;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });
        context = new StubbingContext(rootStubber, StubbingSites.unknown());
    }

    @Test
    void givenCollectionSizeOfZeroItShouldAcceptNotParameterizedList() {
        Type type = new TypeToken<Collection>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldStubNotParameterizedList() {
        Type type = new TypeToken<Collection>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list).isEmpty());
    }

    @Test
    void givenCollectionSizeGreaterThanZeroItShouldNotAcceptNotParameterizedList() {
        Type type = new TypeToken<Collection>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldAcceptParameterizedList() {
        Type type = new TypeToken<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldStubParameterizedList() {
        Type type = new TypeToken<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list).isEmpty());
    }

    @Test
    void givenCollectionSizeGreaterThanZeroItShouldAcceptParameterizedList() {
        Type type = new TypeToken<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenCollectionSizeGreaterThanZeroItShouldStubParameterizedList() {
        Type type = new TypeToken<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list)
                        .hasSize(3)
                        .containsOnly("Test"));
    }

    @Test
    void shouldAcceptListWithUpperBoundedWildcardType() {
        Type type = new TypeToken<Collection<? extends Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubListWithUpperBoundedWildcardType() {
        Type type = new TypeToken<Collection<? extends Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list)
                        .hasSize(3)
                        .containsOnly(1337));
    }

    @Test
    void shouldAcceptListWithLowerBoundedWildcardType() {
        Type type = new TypeToken<Collection<? super Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubListWithLowerBoundedWildcardType() {
        Type type = new TypeToken<Collection<? super Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list)
                        .hasSize(3)
                        .containsOnly(1337));
    }

    @Test
    <T> void shouldAcceptListWithTypeVariable() {
        Type type = new TypeToken<Collection<T>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    <T> void shouldNotAcceptTypeVariable() {
        TypeToken<Collection<T>> token = new TypeToken<Collection<T>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    <T> void shouldThrowExceptionWhenStubbingTypeVariable() {
        TypeToken<Collection<T>> token = new TypeToken<Collection<T>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    <T> void shouldNotAcceptGenericArrayType() {
        TypeToken<Collection<T[]>> token = new TypeToken<Collection<T[]>>() {
        };
        Type type = getTypeArgument(token, 0);
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldNotAcceptCollectionThatIsNoExactMatch() {
        Type type = new TypeToken<ArrayList>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptParameterizedCollectionThatIsNoExactMatch() {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptNonCollectionType() {
        Type type = new TypeToken<Number>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldUseParameterizedTypeStubbingSite() {
        Type type = new TypeToken<Collection<Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 1);

        stubber.stub(context, type);

        verify(rootStubber, times(1)).stub(any(Type.class), any());
        verify(rootStubber).stub((Type) Number.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 0));
    }

}