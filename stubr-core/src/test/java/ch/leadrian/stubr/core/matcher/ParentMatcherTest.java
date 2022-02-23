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
import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParentMatcherTest {

    @Test
    void givenNoParentSiteItShouldNotMatch() {
        StubbingSite site = new TestStubbingSite(null);
        StubbingContext context = mock(StubbingContext.class);
        Matcher<StubbingSite> matcher = Matchers.parent((c, v) -> true);

        boolean matches = matcher.matches(context, site);

        assertThat(matches)
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenParentItShouldDelegateMatching(boolean expectedMatch) {
        StubbingSite parentSite = new TestStubbingSite(null);
        StubbingSite site = new TestStubbingSite(parentSite);
        StubbingContext context = mock(StubbingContext.class);
        @SuppressWarnings("unchecked")
        Matcher<StubbingSite> delegate = mock(Matcher.class);
        when(delegate.matches(any(), any()))
                .thenReturn(expectedMatch);
        Matcher<StubbingSite> matcher = Matchers.parent(delegate);

        boolean matches = matcher.matches(context, site);

        assertAll(
                () -> assertThat(matches).isEqualTo(expectedMatch),
                () -> verify(delegate).matches(context, parentSite)
        );
    }

    private static class TestStubbingSite implements StubbingSite {

        private final StubbingSite parent;

        private TestStubbingSite(StubbingSite parent) {
            this.parent = parent;
        }

        @Override
        public Optional<StubbingSite> getParent() {
            return Optional.ofNullable(parent);
        }

    }

}