package ch.leadrian.stubr.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class TypeMatcherTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void andShouldResolveToTrueIfAndOnlyIfBothMatchersResolveToTrue(boolean firstMatch, boolean secondMatch, boolean expectedResult) {
        TypeMatcher matcher1 = (context, type) -> firstMatch;
        TypeMatcher matcher2 = (context, type) -> secondMatch;
        TypeMatcher andMatcher = matcher1.and(matcher2);

        boolean result = andMatcher.matches(mock(StubbingContext.class), mock(Type.class));

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
        TypeMatcher matcher1 = (context, type) -> firstMatch;
        TypeMatcher matcher2 = (context, type) -> secondMatch;
        TypeMatcher orMatcher = matcher1.or(matcher2);

        boolean result = orMatcher.matches(mock(StubbingContext.class), mock(Type.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void negateShouldReturnResolveToNegatedValue(boolean match, boolean expectedResult) {
        TypeMatcher matcher = (context, type) -> match;
        TypeMatcher negateMatcher = matcher.negate();

        boolean result = negateMatcher.matches(mock(StubbingContext.class), mock(Type.class));

        assertThat(result)
                .isEqualTo(expectedResult);
    }

}