/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class InstanceOfMatcherTest {

    @Nested
    class TestsWithoutDelegate {

        @Test
        void givenExactTypeMatchItShouldReturnTrue() {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(String.class);

            boolean matches = matcher.matches(context, "Test");

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void givenSupertypeMatchItShouldReturnTrue() {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(CharSequence.class);

            boolean matches = matcher.matches(context, "Test");

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void givenNotMatchingTypeItShouldReturnFalse() {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(String.class);

            boolean matches = matcher.matches(context, 1234);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nested
    class TestsWithDelegate {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenExactTypeMatchItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatch) {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(String.class, (c, v) -> delegateMatch);

            boolean matches = matcher.matches(context, "Test");

            assertThat(matches)
                    .isEqualTo(delegateMatch);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenSupertypeMatchItShouldReturnTrue(boolean delegateMatch) {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(CharSequence.class, (c, v) -> delegateMatch);

            boolean matches = matcher.matches(context, "Test");

            assertThat(matches)
                    .isEqualTo(delegateMatch);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNotMatchingTypeItShouldReturnFalse(boolean delegateMatch) {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<Object> matcher = Matchers.instanceOf(String.class, (c, v) -> delegateMatch);

            boolean matches = matcher.matches(context, 1234);

            assertThat(matches)
                    .isFalse();
        }

    }

}