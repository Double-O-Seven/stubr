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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class ArrayStubbingSiteTest {

    @Test
    void shouldReturnParent() {
        StubbingSite expectedParent = mock(StubbingSite.class);
        ArrayStubbingSite site = StubbingSites.array(expectedParent, String.class);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnComponentType() {
        ArrayStubbingSite site = StubbingSites.array(mock(StubbingSite.class), String.class);

        Object componentType = site.getComponentType();

        assertThat(componentType)
                .isEqualTo(String.class);
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() {
        StubbingSite parent1 = mock(StubbingSite.class);
        StubbingSite parent2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(StubbingSites.array(parent1, String.class), StubbingSites.array(parent1, String.class))
                .addEqualityGroup(StubbingSites.array(parent1, Integer.class), StubbingSites.array(parent1, Integer.class))
                .addEqualityGroup(StubbingSites.array(parent2, String.class), StubbingSites.array(parent2, String.class))
                .addEqualityGroup(StubbingSites.array(parent2, Integer.class), StubbingSites.array(parent2, Integer.class))
                .testEquals();
    }

}