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
package ch.leadrian.stubr.javafaker

import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.strategy.StubbingStrategies
import ch.leadrian.stubr.javafaker.JavaFakerStubbingStrategiesTest.Baseline
import ch.leadrian.stubr.junit.StubbingStrategyProvider
import ch.leadrian.stubr.junit.Stubr
import ch.leadrian.stubr.junit.annotation.Stub
import ch.leadrian.stubr.junit.annotation.StubWith
import ch.leadrian.stubr.junit.annotation.StubberBaseline
import ch.leadrian.stubr.junit.annotation.StubberBaseline.Variant.EMPTY
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(Stubr::class)
@StubberBaseline(EMPTY)
@StubWith(Baseline::class)
internal class JavaFakerStubbingStrategiesTest {

    @Test
    @StubWith(FakedData::class)
    fun `should provide faked data`(@Stub testData: TestData, @Stub notFaked: String) {
        assertThat(notFaked).isEqualTo("<not faked>")
        assertAll(
                { assertThat(testData.firstName).isNotEqualTo("<not faked>") },
                { assertThat(testData.lastName).isNotEqualTo("<not faked>") },
                { assertThat(testData.phoneNumber).isNotEqualTo("<not faked>") },
                { assertThat(testData.street).isNotEqualTo("<not faked>") },
                { assertThat(testData.city).isNotEqualTo("<not faked>") },
                { assertThat(testData.zipCode).isNotEqualTo("<not faked>") },
                { assertThat(testData.country).isNotEqualTo("<not faked>") }
        )
    }

    internal class FakedData : StubbingStrategyProvider {

        override fun getStubbingStrategies(extensionContext: ExtensionContext): List<StubbingStrategy> =
                JavaFakerStubbingStrategies.fakedData()
    }

    internal class Baseline : StubbingStrategyProvider {

        override fun getStubbingStrategies(extensionContext: ExtensionContext): List<StubbingStrategy> {
            return listOf(
                    StubbingStrategies.constantValue("<not faked>"),
                    StubbingStrategies.constructor()
            )
        }
    }

    internal class TestData(
            val firstName: String,
            val lastName: String,
            val phoneNumber: String,
            val street: String,
            val city: String,
            val zipCode: String,
            val country: String
    )

}