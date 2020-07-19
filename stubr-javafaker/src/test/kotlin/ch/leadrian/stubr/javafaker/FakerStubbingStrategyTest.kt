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

package ch.leadrian.stubr.javafaker

import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.strategy.StubbingStrategies
import ch.leadrian.stubr.javafaker.FakerStubbingStrategyTest.StringFallback
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
@StubWith(StringFallback::class)
internal class FakerStubbingStrategyTest {

    @Test
    @StubWith(FakedNames::class)
    fun `test faker stubbing strategy`(
            @Stub firstName: String,
            @Stub lastName: CharSequence,
            @Stub somethingElse: String
    ) {
        assertAll(
                { assertThat(firstName).isEqualTo("Hans") },
                { assertThat(lastName).isEqualTo("Wurst") },
                { assertThat(somethingElse).isEqualTo("<not faked>") }
        )
    }

    internal class FakedNames : StubbingStrategyProvider {

        override fun getStubbingStrategies(extensionContext: ExtensionContext): List<StubbingStrategy> {
            val firstName = FakerStrategy.builder()
                    .accept("first", "name")
                    .build { _ -> "Hans" }
            val lastName = FakerStrategy.builder()
                    .accept("last", "name")
                    .build { _ -> "Wurst" }
            return listOf(
                    JavaFakerStubbingStrategies.faked(firstName),
                    JavaFakerStubbingStrategies.faked(lastName)
            )
        }
    }

    internal class StringFallback : StubbingStrategyProvider {

        override fun getStubbingStrategies(extensionContext: ExtensionContext): List<StubbingStrategy> =
                listOf(StubbingStrategies.constantValue("<not faked>"))
    }
}