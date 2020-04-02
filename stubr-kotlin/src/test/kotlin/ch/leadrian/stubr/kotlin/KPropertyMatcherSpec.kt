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

package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.site.StubbingSites
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object KPropertyMatcherSpec : Spek({

    describe("matches") {

        context("given site is not a KPropertyStubbingSite") {
            val site = StubbingSites.unknown()
            val context by memoized { StubbingContext.create(mock(Stubber::class.java), site) }
            val matcher = KotlinMatchers.kotlinPropertyIs<Any?> { _, _ -> true }

            it("should not match") {
                assertThat(matcher.matches(context, "Test"))
                        .isFalse()
            }
        }

        context("given site is a KPropertyStubbingSite") {
            val site = KPropertyStubbingSite(Foo("foo"), Foo::foo)
            val context by memoized { StubbingContext.create(mock(Stubber::class.java), site) }

            context("given delegate matcher does not match") {
                val matcher = KotlinMatchers.kotlinPropertyIs<Any?> { _, _ -> false }

                it("should not match") {
                    assertThat(matcher.matches(context, "Test"))
                            .isFalse()
                }
            }

            context("given delegate matcher matches") {
                val matcher = KotlinMatchers.kotlinPropertyIs<Any?> { _, property -> property == Foo::foo }

                it("should match") {
                    assertThat(matcher.matches(context, "Test"))
                            .isTrue()
                }
            }
        }
    }
}) {

    private data class Foo(val foo: String)

}