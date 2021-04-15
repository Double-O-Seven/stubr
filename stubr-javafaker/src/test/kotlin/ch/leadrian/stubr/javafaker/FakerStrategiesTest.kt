/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.StubbingContext
import com.github.javafaker.Faker
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class FakerStrategiesTest {

    @Nested
    internal inner class FirstNameTests {

        @ParameterizedTest
        @CsvSource(
                "'firstName', true",
                "'firstname', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.firstName().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { name().firstName() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.firstName().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class LastNameTests {

        @ParameterizedTest
        @CsvSource(
                "'lastName', true",
                "'lastname', true",
                "'surname', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.lastName().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { name().lastName() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.lastName().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class PhoneNumberTests {

        @ParameterizedTest
        @CsvSource(
                "'phoneNumber', true",
                "'phonenumber', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.phoneNumber().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { phoneNumber().phoneNumber() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.phoneNumber().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class StreetTests {

        @ParameterizedTest
        @CsvSource(
                "'street', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.street().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { address().streetName() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.street().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class CityTests {

        @ParameterizedTest
        @CsvSource(
                "'city', true",
                "'town', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.city().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { address().city() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.city().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class ZipCodeTests {

        @ParameterizedTest
        @CsvSource(
                "'zipCode', true",
                "'zipcode', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.zipCode().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { address().zipCode() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.zipCode().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }

    @Nested
    internal inner class CountryTests {

        @ParameterizedTest
        @CsvSource(
                "'country', true",
                "'bla', false"
        )
        fun `should only accept values acceptable values`(wordSequence: WordSequence, expectedAccepts: Boolean) {
            val accepts = FakerStrategies.country().accepts(wordSequence)

            assertThat(accepts).isEqualTo(expectedAccepts)
        }

        @Test
        fun `should provide faked value`() {
            val faker = mockk<Faker> {
                every { address().country() } returns "faked"
            }
            val wordSequence = WordSequence.extractFrom("test")
            val context = mockk<StubbingContext>()

            val result = FakerStrategies.country().fake(faker, wordSequence, context)

            assertThat(result).isEqualTo("faked")
        }

    }
}