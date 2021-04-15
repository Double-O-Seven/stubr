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

package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.site.StubbingSites
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.properties.ReadOnlyProperty

internal object StubberSpec : Spek({
    val stubber by memoized {
        Stubber.builder()
                .stubWith(KotlinStubbingStrategies.constantValue(1337))
                .stubWith(KotlinStubbingStrategies.suppliedValue { sequenceNumber -> sequenceNumber.toString() })
                .build()
    }

    describe("tryToStub") {

        context("given site") {

            context("given stubbable type") {

                it("should return success") {
                    val value = stubber.tryToStub<Int>(StubbingSites.unknown())
                    assertThat(value)
                            .isEqualTo(Result.success(1337))
                }

            }

            context("given unknown type") {

                it("should return failure") {
                    val value = stubber.tryToStub<Long>(StubbingSites.unknown())
                    assertThat(value)
                            .isEqualTo(Result.failure<Long>())
                }

            }

        }

        context("given no site") {

            context("given stubbable type") {

                it("should return success") {
                    val value = stubber.tryToStub<Int>()
                    assertThat(value)
                            .isEqualTo(Result.success(1337))
                }

            }

            context("given unknown type") {

                it("should return failure") {
                    val value = stubber.tryToStub<Long>()
                    assertThat(value)
                            .isEqualTo(Result.failure<Long>())
                }

            }

        }

    }

    describe("stub") {

        context("given site") {

            it("should return stub value") {
                val value: Int = stubber.stub(StubbingSites.unknown())
                assertThat(value)
                        .isEqualTo(1337)
            }

        }

        context("given no site") {

            it("should return stub value") {
                val value: Int = stubber.stub()
                assertThat(value)
                        .isEqualTo(1337)
            }

        }

    }

    describe("getValue") {

        it("should return new stub value for each property read access") {
            val value: String by stubber

            val values = listOf(value, value)

            assertThat(values)
                    .containsExactly("0", "1")
        }
    }

    describe("stubbing") {
        val propertyDelegate by memoized { stubber.stubbing<String>() }
        val delegatingObject by memoized { DelegatingObject(propertyDelegate) }

        it("should memoize the stub value") {
            val values = (0..2).map { delegatingObject.foo }
            assertThat(values).hasSize(3).containsOnly("0")
        }

        context("given multiple properties") {

            it("should not reuse stub values for different owners") {
                assertAll(
                        { assertThat(delegatingObject.foo).isEqualTo("0") },
                        { assertThat(delegatingObject.bar).isEqualTo("1") }
                )
            }
        }

        context("given multiple property owners") {
            val otherDelegatingObject by memoized { DelegatingObject(propertyDelegate) }

            it("should not reuse stub values for different owners") {
                assertAll(
                        { assertThat(delegatingObject.foo).isEqualTo("0") },
                        { assertThat(otherDelegatingObject.foo).isEqualTo("1") }
                )
            }
        }
    }

}) {

    private class DelegatingObject(propertyDelegate: ReadOnlyProperty<Any?, String>) {

        val foo by propertyDelegate

        val bar by propertyDelegate

    }

}
