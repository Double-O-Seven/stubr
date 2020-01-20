package ch.leadrian.stubr.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StubberTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void whenStubberShouldAcceptIfAndOnlyIfReceiverStubberAndTypeMatcherAccept(boolean receiverAccepts, boolean typeMatches, boolean expectedResult) {
        Stubber stubber = new Stubber() {

            @Override
            public boolean accepts(StubbingContext context, Type type) {
                return receiverAccepts;
            }

            @Override
            public Object stub(StubbingContext context, Type type) {
                return null;
            }
        };
        Stubber whenStubber = stubber.when(((context, type) -> typeMatches));

        boolean result = whenStubber.accepts(mock(StubbingContext.class), String.class);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnStubValueOfReceiverStub() {
        Stubber stubber = new Stubber() {

            @Override
            public boolean accepts(StubbingContext context, Type type) {
                return true;
            }

            @Override
            public Object stub(StubbingContext context, Type type) {
                return "Test";
            }
        };
        Stubber whenStubber = stubber.when(((context, type) -> true));

        Object result = whenStubber.stub(mock(StubbingContext.class), String.class);

        assertThat(result)
                .isEqualTo("Test");
    }

}