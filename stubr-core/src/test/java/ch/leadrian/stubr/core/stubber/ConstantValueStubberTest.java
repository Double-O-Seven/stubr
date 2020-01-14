package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.type.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ConstantValueStubberTest {

    private StubbingContext context = new StubbingContext(mock(RootStubber.class), StubbingSites.unknown());

    @Test
    void shouldAcceptExactClass() {
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        boolean accepts = stubber.accepts(context, Number.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldAcceptWildcardTypeWithUpperBound() {
        Type type = new ParameterizedTypeLiteral<List<? extends Number>>() {
        }.getActualTypeArgument(0);
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldAcceptWildcardTypeWithLowerBound() {
        Type type = new ParameterizedTypeLiteral<List<? super Number>>() {
        }.getActualTypeArgument(0);
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldNotAcceptSubclass() {
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        boolean accepts = stubber.accepts(context, Integer.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldNotAcceptSuperclass() {
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        boolean accepts = stubber.accepts(context, Object.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void shouldAcceptUpperBoundParameterizedType() {
        Type acceptedType = new TypeLiteral<List<String>>() {
        }.getType();
        Type type = new ParameterizedTypeLiteral<List<? extends List<String>>>() {
        }.getActualTypeArgument(0);
        ConstantValueStubber stubber = new ConstantValueStubber(acceptedType, singletonList("ABC"));

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldAcceptLowerBoundParameterizedType() {
        Type acceptedType = new TypeLiteral<List<String>>() {
        }.getType();
        Type type = new ParameterizedTypeLiteral<List<? super List<String>>>() {
        }.getActualTypeArgument(0);
        ConstantValueStubber stubber = new ConstantValueStubber(acceptedType, singletonList("ABC"));

        boolean accepts = stubber.accepts(context, type);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldReturnValue() {
        ConstantValueStubber stubber = new ConstantValueStubber(Number.class, 1337);

        Object stub = stubber.stub(context, Number.class);

        assertThat(stub)
                .isEqualTo(1337);
    }

}