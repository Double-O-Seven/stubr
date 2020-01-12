package ch.leadrian.stubr.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ConstructorMatcherTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void andShouldResolveToTrueIfAndOnlyIfBothMatchersResolveToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        ConstructorMatcher matcher1 = constructor -> firstMatch;
        ConstructorMatcher matcher2 = constructor -> secondMatch;
        ConstructorMatcher andMatcher = matcher1.and(matcher2);

        boolean result = andMatcher.matches(mock(Constructor.class));

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
        ConstructorMatcher matcher1 = constructor -> firstMatch;
        ConstructorMatcher matcher2 = constructor -> secondMatch;
        ConstructorMatcher orMatcher = matcher1.or(matcher2);

        boolean result = orMatcher.matches(mock(Constructor.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void negateShouldReturnResolveToNegatedValue(boolean match, boolean expectedResult) {
        ConstructorMatcher matcher = constructor -> match;
        ConstructorMatcher negateMatcher = matcher.negate();

        boolean result = negateMatcher.matches(mock(Constructor.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

}