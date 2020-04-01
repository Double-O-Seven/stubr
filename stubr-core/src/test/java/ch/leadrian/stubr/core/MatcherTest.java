package ch.leadrian.stubr.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MatcherTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void andShouldResolveToTrueIfAndOnlyIfBothMatchersResolveToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        Matcher<Object> matcher1 = (context, value) -> firstMatch;
        Matcher<Object> matcher2 = (context, value) -> secondMatch;
        Matcher<Object> andMatcher = matcher1.and(matcher2);

        boolean result = andMatcher.matches(mock(StubbingContext.class), new Object());

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, true",
            "true, false, true",
            "false, false, false"
    })
    void orShouldResolveToTrueIfAndOnlyIfAtLeastOneMatcherResolvesToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        Matcher<Object> matcher1 = (context, value) -> firstMatch;
        Matcher<Object> matcher2 = (context, value) -> secondMatch;
        Matcher<Object> orMatcher = matcher1.or(matcher2);

        boolean result = orMatcher.matches(mock(StubbingContext.class), new Object());

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void negateShouldReturnResolveToNegatedValue(boolean match, boolean expectedResult) {
        Matcher<Object> matcher = (context, value) -> match;
        Matcher<Object> negateMatcher = matcher.negate();

        boolean result = negateMatcher.matches(mock(StubbingContext.class), new Object());

        assertThat(result)
                .isEqualTo(expectedResult);
    }

}