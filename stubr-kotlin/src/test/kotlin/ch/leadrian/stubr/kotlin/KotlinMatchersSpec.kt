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

package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Matcher
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.site.StubbingSites
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.reflect.jvm.javaMethod

internal object KotlinMatchersSpec : Spek({

    describe("kotlinTypeIsNullable") {

        context("given MethodReturnValueStubbingSite") {

            context("given nullable return type") {
                val method = KotlinMatchersSpec::nullableValue.javaMethod
                val site = StubbingSites.methodReturnValue(StubbingSites.unknown(), method)
                val context = mock(StubbingContext::class.java)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isTrue()
                }
            }

            context("given non-null return type") {
                val method = KotlinMatchersSpec::nonNullValue.javaMethod
                val site = StubbingSites.methodReturnValue(StubbingSites.unknown(), method)
                val context = mock(StubbingContext::class.java)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isFalse()
                }
            }

        }

        context("given MethodParameterStubbingSite") {

            context("given nullable parameter type") {
                val function = KotlinMatchersSpec::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 0)
                val context = mock(StubbingContext::class.java)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isTrue()
                }
            }

            context("given non-null parameter type") {
                val function = KotlinMatchersSpec::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 1)
                val context = mock(StubbingContext::class.java)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isFalse()
                }
            }

            context("given java method") {
                val function = KotlinMatchersTestFixtures::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 0)
                val context = mock(StubbingContext::class.java)

                beforeEachTest {
                    require(function.parameters.size == 1)
                }

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isFalse()
                }
            }
        }

        context("given ConstructorParameterStubbingSite") {

            context("given nullable parameter type") {
                val constructor = Foo::class.java.getDeclaredConstructor(Any::class.java, Any::class.java)
                val site = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, 0)
                val context = mock(StubbingContext::class.java)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isTrue()
                }
            }

            context("given non-null parameter type") {
                val constructor = Foo::class.java.getDeclaredConstructor(Any::class.java, Any::class.java)
                val site = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, 1)
                val context = mock(StubbingContext::class.java)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeNullable<StubbingSite>().matches(context, site))
                        .isFalse()
                }
            }
        }
    }

    describe("kotlinProperty") {

        context("given site is not a KPropertyStubbingSite") {
            val site = StubbingSites.unknown()
            val context by memoized { mock(StubbingContext::class.java) }
            val matcher = KotlinMatchers.kotlinProperty<StubbingSite> { _, _ -> true }

            it("should not match") {
                assertThat(matcher.matches(context, site))
                    .isFalse()
            }
        }

        context("given site is a KPropertyStubbingSite") {
            val site = KPropertyStubbingSite(Fubar("foo"), Fubar::foo)
            val context by memoized { mock(StubbingContext::class.java) }

            context("given delegate matcher does not match") {
                val matcher = KotlinMatchers.kotlinProperty<StubbingSite> { _, _ -> false }

                it("should not match") {
                    assertThat(matcher.matches(context, site))
                        .isFalse()
                }
            }

            context("given delegate matcher matches") {
                val matcher = KotlinMatchers.kotlinProperty<StubbingSite> { _, property -> property == Fubar::foo }

                it("should match") {
                    assertThat(matcher.matches(context, site))
                        .isTrue()
                }
            }
        }
    }

    describe("instanceOf") {
        val context by memoized { mock(StubbingContext::class.java) }

        listOf(true, false).forEach { expectedResult ->
            context("given delegate returning $expectedResult") {
                val matcher: Matcher<Any> =
                    KotlinMatchers.instanceOf { _: StubbingContext, _: String -> expectedResult }

                context("given value is instance of target class") {

                    it("should return $expectedResult") {
                        assertThat(matcher.matches(context, "Test"))
                            .isEqualTo(expectedResult)
                    }
                }

                context("given value is not instance of target class") {

                    it("should return false") {
                        assertThat(matcher.matches(context, 1234))
                            .isFalse()
                    }
                }
            }
        }

        context("given no delegate") {
            val matcher = KotlinMatchers.instanceOf<Any, String>()

            context("given value is instance of target class") {

                it("should return true") {
                    assertThat(matcher.matches(context, "Test"))
                        .isTrue()
                }
            }

            context("given value is not instance of target class") {

                it("should return false") {
                    assertThat(matcher.matches(context, 1234))
                        .isFalse()
                }
            }
        }
    }
}) {

    private fun nullableValue(): Any? = null

    private fun nonNullValue(): Any = ""

    @Suppress("unused", "UNUSED_PARAMETER")
    private fun testParameters(nullableParam: Any?, nonNullParam: Any) {
    }

    @Suppress("UNUSED_PARAMETER")
    class Foo(nullableParam: Any?, nonNullParam: Any)

    private data class Fubar(val foo: String)

}