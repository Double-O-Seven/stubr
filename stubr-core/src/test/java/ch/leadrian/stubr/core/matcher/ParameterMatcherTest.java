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

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.ParameterStubbingSite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ParameterMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenParameterStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
        StubbingContext context = StubbingContext.create(mock(Stubber.class), mock(ParameterStubbingSite.class));
        Matcher<Object> matcher = Matchers.parameterIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isEqualTo(delegateMatches);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoParameterStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
        StubbingContext context = StubbingContext.create(mock(Stubber.class), mock(StubbingSite.class));
        Matcher<Object> matcher = Matchers.parameterIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isFalse();
    }

}