package ch.leadrian.stubr.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ParameterMatcherTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void andShouldResolveToTrueIfAndOnlyIfBothMatchersResolveToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        ParameterMatcher matcher1 = (context, parameter) -> firstMatch;
        ParameterMatcher matcher2 = (context, parameter) -> secondMatch;
        ParameterMatcher andMatcher = matcher1.and(matcher2);

        boolean result = andMatcher.matches(mock(StubbingContext.class), mock(Parameter.class));

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
        ParameterMatcher matcher1 = (context, parameter) -> firstMatch;
        ParameterMatcher matcher2 = (context, parameter) -> secondMatch;
        ParameterMatcher orMatcher = matcher1.or(matcher2);

        boolean result = orMatcher.matches(mock(StubbingContext.class), mock(Parameter.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void negateShouldReturnResolveToNegatedValue(boolean match, boolean expectedResult) {
        ParameterMatcher matcher = (context, parameter) -> match;
        ParameterMatcher negateMatcher = matcher.negate();

        boolean result = negateMatcher.matches(mock(StubbingContext.class), mock(Parameter.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @AfterEach
    void clearMocks() {
        Mockito.framework().clearInlineMocks();
    }

}