package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConditionalStubberTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "true, false, false",
            "false, true, false",
            "false, false, false"
    })
    void shouldAcceptIfAndOnlyIfDelegateAcceptsAndMatcherMatches(boolean delegateMatch, boolean matcherMatch, boolean expectedResult) {
        StubbingContext context = mock(StubbingContext.class);
        Type type = String.class;
        Stubber delegate = mock(Stubber.class);
        when(delegate.accepts(context, type))
                .thenReturn(delegateMatch);
        Matcher<? super Type> matcher = (c, t) -> matcherMatch;
        ConditionalStubber stubber = new ConditionalStubber(delegate, matcher);

        boolean result = stubber.accepts(context, type);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnStubbedValueFromDelegate() {
        StubbingContext context = mock(StubbingContext.class);
        Type type = String.class;
        Stubber delegate = mock(Stubber.class);
        when(delegate.stub(context, type))
                .thenReturn("Test");
        ConditionalStubber stubber = new ConditionalStubber(delegate, (c, t) -> true);

        Object stub = stubber.stub(context, type);

        assertThat(stub)
                .isEqualTo("Test");
    }

}