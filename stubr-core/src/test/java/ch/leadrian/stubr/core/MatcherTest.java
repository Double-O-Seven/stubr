/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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