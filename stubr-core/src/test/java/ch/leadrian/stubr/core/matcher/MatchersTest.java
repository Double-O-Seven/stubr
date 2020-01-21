package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MatchersTest {

    @Test
    void anyShouldReturnTrue() {
        Matcher<Object> matcher = Matchers.any();

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void notShouldNegateCondition(boolean inputValue, boolean expectedValue) {
        Matcher<Object> matcher = Matchers.not(((context, value) -> inputValue));

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isEqualTo(expectedValue);
    }
}
