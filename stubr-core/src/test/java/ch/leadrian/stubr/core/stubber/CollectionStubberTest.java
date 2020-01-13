package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.util.TypeLiteral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import static ch.leadrian.stubr.core.util.Types.getRawType;
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
                    Class<?> rawType = getRawType(invocation.getArgument(0, Type.class)).orElseThrow(UnsupportedOperationException::new);
                    if (rawType == String.class) {
                        return "Test";
                    } else if (rawType == Number.class) {
                        return 1337;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                });
        context = new StubbingContext(rootStubber, StubbingSites.unknown());
    }

    @Test
    void givenCollectionSizeOfZeroItShouldAcceptNotParameterizedList() {
        Type type = Collection.class;
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldStubNotParameterizedList() {
        Type type = Collection.class;
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list).isEmpty());
    }

    @Test
    void givenCollectionSizeGreaterThanZeroItShouldNotAcceptNotParameterizedList() {
        Type type = Collection.class;
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldAcceptParameterizedList() {
        Type type = new TypeLiteral<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenCollectionSizeOfZeroItShouldStubParameterizedList() {
        Type type = new TypeLiteral<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 0);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isInstanceOfSatisfying(ArrayList.class, list -> assertThat(list).isEmpty());
    }

    @Test
    void givenCollectionSizeGreaterThanZeroItShouldAcceptParameterizedList() {
        Type type = new TypeLiteral<Collection<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void givenCollectionSizeGreaterThanZeroItShouldStubParameterizedList() {
        Type type = new TypeLiteral<Collection<String>>() {
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
        Type type = new TypeLiteral<Collection<? extends Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubListWithUpperBoundedWildcardType() {
        Type type = new TypeLiteral<Collection<? extends Number>>() {
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
        Type type = new TypeLiteral<Collection<? super Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldStubListWithLowerBoundedWildcardType() {
        Type type = new TypeLiteral<Collection<? super Number>>() {
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
        Type type = new TypeLiteral<Collection<T>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    <T> void shouldNotAcceptTypeVariable() {
        Type type = new TypeLiteral<T>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    <T> void shouldThrowExceptionWhenStubbingTypeVariable() {
        Type type = new TypeLiteral<T>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    <T> void shouldNotAcceptGenericArrayType() {
        Type type = new TypeLiteral<T[]>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    <T> void shouldThrowExceptionWhenStubbingGenericArrayType() {
        Type type = new TypeLiteral<T[]>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldNotAcceptCollectionThatIsNoExactMatch() {
        Type type = ArrayList.class;
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptParameterizedCollectionThatIsNoExactMatch() {
        Type type = new TypeLiteral<ArrayList<String>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptNonCollectionType() {
        Type type = Number.class;
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 3);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldUseParameterizedTypeStubbingSite() {
        Type type = new TypeLiteral<Collection<Number>>() {
        }.getType();
        Stubber stubber = new CollectionStubber<>(Collection.class, ArrayList::new, context -> 1);

        stubber.stub(context, type);

        verify(rootStubber, times(1)).stub(any(Type.class), any());
        verify(rootStubber).stub((Type) Number.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 0));
    }

}