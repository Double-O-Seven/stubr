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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MemoizingStubbingSiteTest {

    @Test
    void shouldReturnParent() {
        StubbingSite expectedParent = mock(StubbingSite.class);
        MemoizingStubbingSite site = StubbingSites.memoizing(expectedParent);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() {
        StubbingSite parent1 = mock(StubbingSite.class);
        StubbingSite parent2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(StubbingSites.memoizing(parent1), StubbingSites.memoizing(parent1))
                .addEqualityGroup(StubbingSites.memoizing(parent2), StubbingSites.memoizing(parent2))
                .testEquals();
    }

}