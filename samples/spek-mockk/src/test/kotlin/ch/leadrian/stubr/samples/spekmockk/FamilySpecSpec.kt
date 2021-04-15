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

package ch.leadrian.stubr.samples.spekmockk

import ch.leadrian.stubr.samples.models.Family
import ch.leadrian.stubr.samples.models.FamilyFactory
import ch.leadrian.stubr.samples.models.FamilyService
import ch.leadrian.stubr.samples.models.Person
import ch.leadrian.stubr.spek.memoizedStub
import ch.leadrian.stubr.spek.useStubber
import io.mockk.every
import io.mockk.isMockKMock
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object FamilySpecSpec : Spek({
    useStubber { familyStubber() }
    val familyFactory by mockedTestDependency<FamilyFactory>()
    val familyService by testSubject<FamilyService>()

    describe("performConservativeMarriage") {
        val mom by memoizedStub<Person>()
        val dad by memoizedStub<Person>()
        val expectedFamily by memoizedStub<Family>()
        lateinit var family: Family

        beforeEachTest {
            every { familyFactory.create(mom, dad) } returns expectedFamily
            family = familyService.performConservativeMarriage(mom, dad)
        }

        it("should return expected family") {
            assertThat(family).isEqualTo(expectedFamily)
        }

        it("should reuse familyFactory") {
            assertThat(familyService.familyFactory).isSameAs(familyFactory)
        }

        it("should should not mock test subject") {
            assertThat(isMockKMock(familyService)).isFalse
        }

        it("should should mock dependency") {
            assertThat(isMockKMock(familyFactory)).isTrue
        }

        it("should should not mock test data") {
            assertThat(isMockKMock(mom)).isFalse
            assertThat(isMockKMock(dad)).isFalse
            assertThat(isMockKMock(family)).isFalse
        }
    }
})