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
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SiteMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnTrueIfAndOnlyIfDelegateReturnsTrue(boolean expectedResult) {
        StubbingSite site = mock(StubbingSite.class);
        StubbingContext context = mock(StubbingContext.class);
        when(context.getSite())
                .thenReturn(site);
        @SuppressWarnings("unchecked")
        Matcher<StubbingSite> delegate = (Matcher<StubbingSite>) mock(Matcher.class);
        when(delegate.matches(context, site))
                .thenReturn(expectedResult);
        Matcher<Object> matcher = Matchers.site(delegate);

        boolean result = matcher.matches(context, "test");

        assertThat(result)
                .isEqualTo(expectedResult);
    }

}