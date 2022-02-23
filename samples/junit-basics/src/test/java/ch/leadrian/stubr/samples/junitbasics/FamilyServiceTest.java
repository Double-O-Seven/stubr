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

package ch.leadrian.stubr.samples.junitbasics;

import ch.leadrian.stubr.junit.Stubr;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.Stub;
import ch.leadrian.stubr.samples.models.Family;
import ch.leadrian.stubr.samples.models.FamilyFactory;
import ch.leadrian.stubr.samples.models.FamilyService;
import ch.leadrian.stubr.samples.models.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A basic example of how stubs can be used with JUnit 5.
 *
 * @see FamilyStubs
 */
@ExtendWith(Stubr.class)
@Include(FamilyStubs.class)
class FamilyServiceTest {

    @Test
    void shouldCreateFamily(
            @Stub Person mom,
            @Stub Person dad,
            @Stub Family expectedFamily
    ) {
        FamilyFactory familyFactory = mock(FamilyFactory.class);
        when(familyFactory.create(mom, dad)).thenReturn(expectedFamily);
        FamilyService familyService = new FamilyService(familyFactory);

        Family family = familyService.performConservativeMarriage(mom, dad);

        assertThat(mom).isNotEqualTo(dad);
        assertThat(family)
                .isSameAs(expectedFamily);
    }

}
