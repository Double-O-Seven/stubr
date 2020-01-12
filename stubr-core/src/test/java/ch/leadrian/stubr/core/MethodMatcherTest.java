package ch.leadrian.stubr.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MethodMatcherTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void andShouldResolveToTrueIfAndOnlyIfBothMatchersResolveToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        MethodMatcher matcher1 = method -> firstMatch;
        MethodMatcher matcher2 = method -> secondMatch;
        MethodMatcher andMatcher = matcher1.and(matcher2);

        boolean result = andMatcher.matches(mock(Method.class));

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
        MethodMatcher matcher1 = method -> firstMatch;
        MethodMatcher matcher2 = method -> secondMatch;
        MethodMatcher orMatcher = matcher1.or(matcher2);

        boolean result = orMatcher.matches(mock(Method.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void negateShouldReturnResolveToNegatedValue(boolean match, boolean expectedResult) {
        MethodMatcher matcher = method -> match;
        MethodMatcher negateMatcher = matcher.negate();

        boolean result = negateMatcher.matches(mock(Method.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

}