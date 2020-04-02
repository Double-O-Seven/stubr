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

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.site.StubbingSites
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.reflect.Type
import kotlin.reflect.jvm.javaMethod

internal object KotlinMatchersSpec : Spek({

    describe("kotlinTypeIsNullable") {

        context("given MethodReturnValueStubbingSite") {

            context("given nullable return type") {
                val method = KotlinMatchersSpec::nullableValue.javaMethod
                val site = StubbingSites.methodReturnValue(StubbingSites.unknown(), method)
                val context = StubbingContext.create(TestStubber, site)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isTrue()
                }
            }

            context("given non-null return type") {
                val method = KotlinMatchersSpec::nonNullValue.javaMethod
                val site = StubbingSites.methodReturnValue(StubbingSites.unknown(), method)
                val context = StubbingContext.create(TestStubber, site)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isFalse()
                }
            }

        }

        context("given MethodParameterStubbingSite") {

            context("given nullable parameter type") {
                val function = KotlinMatchersSpec::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 0)
                val context = StubbingContext.create(TestStubber, site)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isTrue()
                }
            }

            context("given non-null parameter type") {
                val function = KotlinMatchersSpec::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 1)
                val context = StubbingContext.create(TestStubber, site)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isFalse()
                }
            }

            context("given java method") {
                val function = KotlinMatchersTestFixtures::testParameters
                val site = StubbingSites.methodParameter(StubbingSites.unknown(), function.javaMethod, 0)
                val context = StubbingContext.create(TestStubber, site)

                beforeEachTest {
                    require(function.parameters.size == 1)
                }

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isFalse()
                }
            }
        }

        context("given ConstructorParameterStubbingSite") {

            context("given nullable parameter type") {
                val constructor = Foo::class.java.getDeclaredConstructor(Any::class.java, Any::class.java)
                val site = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, 0)
                val context = StubbingContext.create(TestStubber, site)

                it("should match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
                            .isTrue()
                }
            }

            context("given non-null parameter type") {
                val constructor = Foo::class.java.getDeclaredConstructor(Any::class.java, Any::class.java)
                val site = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, 1)
                val context = StubbingContext.create(TestStubber, site)

                it("should not match") {
                    assertThat(KotlinMatchers.kotlinTypeIsNullable<Any>().matches(context, null))
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

    object TestStubber : Stubber() {

        override fun tryToStub(type: Type, context: StubbingContext): Result<Any> = Result.failure()

    }

    @Suppress("UNUSED_PARAMETER")
    class Foo(nullableParam: Any?, nonNullParam: Any)

}