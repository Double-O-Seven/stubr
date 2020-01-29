package ch.leadrian.stubr.core;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CompositeRootStubbingStrategyTest {

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnResultOfFirstSuccessfulStubber() {
        Class<String> type = String.class;
        StubbingContext context = mock(StubbingContext.class);
        Stubber stubber1 = mock(Stubber.class);
        when(stubber1.tryToStub(type, context))
                .thenReturn(Result.failure());
        Stubber stubber2 = mock(Stubber.class);
        when(stubber2.tryToStub(type, context))
                .thenReturn((Result) Result.success("Test"));
        Stubber stubber3 = mock(Stubber.class);
        when(stubber3.tryToStub(type, context))
                .thenReturn((Result) Result.success("Foo"));
        Stubber compositeStubber = Stubber.compose(stubber1, stubber2, stubber3);

        Result<?> result = compositeStubber.tryToStub(type, context);

        assertAll(
                () -> assertThat(result.getValue()).isEqualTo("Test"),
                () -> {
                    InOrder inOrder = inOrder(stubber1, stubber2);
                    inOrder.verify(stubber1).tryToStub(type, context);
                    inOrder.verify(stubber2).tryToStub(type, context);
                },
                () -> verifyNoInteractions(stubber3)
        );
    }

}