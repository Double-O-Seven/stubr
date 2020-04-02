/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.testing.ParameterizedTypeLiteral;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class ParameterizedTypeStubbingSiteTest {

    @Test
    void shouldReturnParent() {
        StubbingSite expectedParent = mock(StubbingSite.class);
        ParameterizedType type = new ParameterizedTypeLiteral<List<String>>() {}.getType();
        ParameterizedTypeStubbingSite site = StubbingSites.parameterizedType(expectedParent, type, 0);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnType() {
        StubbingSite parent = mock(StubbingSite.class);
        ParameterizedType expectedType = new ParameterizedTypeLiteral<List<String>>() {}.getType();
        ParameterizedTypeStubbingSite site = StubbingSites.parameterizedType(parent, expectedType, 0);

        ParameterizedType type = site.getType();

        assertThat(type)
                .isEqualTo(type);
    }

    @Test
    void shouldReturnParameterIndex() {
        StubbingSite parent = mock(StubbingSite.class);
        ParameterizedType type = new ParameterizedTypeLiteral<List<String>>() {}.getType();
        ParameterizedTypeStubbingSite site = StubbingSites.parameterizedType(parent, type, 0);

        int parameterIndex = site.getTypeArgumentIndex();

        assertThat(parameterIndex)
                .isZero();
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() {
        StubbingSite parent1 = mock(StubbingSite.class);
        ParameterizedType type1 = new ParameterizedTypeLiteral<List<String>>() {}.getType();
        StubbingSite parent2 = mock(StubbingSite.class);
        ParameterizedType type2 = new ParameterizedTypeLiteral<Set<Integer>>() {}.getType();

        new EqualsTester()
                .addEqualityGroup(StubbingSites.parameterizedType(parent1, type1, 0), StubbingSites.parameterizedType(parent1, type1, 0))
                .addEqualityGroup(StubbingSites.parameterizedType(parent2, type2, 1337), StubbingSites.parameterizedType(parent2, type2, 1337))
                .testEquals();
    }

}