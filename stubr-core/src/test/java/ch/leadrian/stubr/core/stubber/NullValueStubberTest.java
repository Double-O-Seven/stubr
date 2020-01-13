package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NullValueStubberTest {

    private StubbingContext context = new StubbingContext(mock(RootStubber.class), StubbingSites.unknown());

    @Test
    void shouldAcceptAnything() {
        boolean accepts = NullValueStubber.INSTANCE.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void shouldReturnNullAsStub() {
        Object stub = NullValueStubber.INSTANCE.stub(context, Foo.class);

        assertThat(stub)
                .isNull();
    }

    private static final class Foo {
    }

}