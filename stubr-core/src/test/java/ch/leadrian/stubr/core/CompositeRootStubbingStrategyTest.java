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
    void shouldReturnResultOfFirstSuccessfulRootStubber() {
        Class<String> type = String.class;
        StubbingContext context = mock(StubbingContext.class);
        RootStubber rootStubber1 = mock(RootStubber.class);
        when(rootStubber1.tryToStub(type, context))
                .thenReturn(Result.failure());
        RootStubber rootStubber2 = mock(RootStubber.class);
        when(rootStubber2.tryToStub(type, context))
                .thenReturn((Result) Result.success("Test"));
        RootStubber rootStubber3 = mock(RootStubber.class);
        when(rootStubber3.tryToStub(type, context))
                .thenReturn((Result) Result.success("Foo"));
        RootStubber compositeRootStubber = RootStubber.compose(rootStubber1, rootStubber2, rootStubber3);

        Result<?> result = compositeRootStubber.tryToStub(type, context);

        assertAll(
                () -> assertThat(result.getValue()).isEqualTo("Test"),
                () -> {
                    InOrder inOrder = inOrder(rootStubber1, rootStubber2);
                    inOrder.verify(rootStubber1).tryToStub(type, context);
                    inOrder.verify(rootStubber2).tryToStub(type, context);
                },
                () -> verifyNoInteractions(rootStubber3)
        );
    }

}