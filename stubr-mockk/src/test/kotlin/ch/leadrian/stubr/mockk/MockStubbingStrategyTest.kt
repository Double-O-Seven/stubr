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

package ch.leadrian.stubr.mockk

import ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester
import io.mockk.MockKException
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertAll
import java.util.stream.Stream

internal class MockStubbingStrategyTest {

    @TestFactory
    fun `test mockk stubbing strategy with default configuration`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(Foo::class.java)
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(Foo::class.java) { foo ->
                        assertAll(
                                { assertThat(foo.getInt()).isZero() },
                                { assertThat(foo.getString()).isEmpty() }
                        )
                    }
                }
                .rejects(Baz::class.java)
                .rejects(Int::class.javaPrimitiveType)
                .rejects(Array<Any>::class.java)
                .rejects(Qux::class.java)
                .test(MockKStubbingStrategies.mockk<Foo>())
    }

    @TestFactory
    fun `test mockk stubbing strategy with configuration adjustments`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(Foo::class.java)
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(Foo::class.java) { foo ->
                        assertAll(
                                { assertThat(foo.getInt()).isEqualTo(1337) },
                                { assertThat(catchThrowable { foo.getString() }).isInstanceOf(MockKException::class.java) }
                        )
                    }
                }
                .test(MockKStubbingStrategies.mockk<Foo>(relaxed = false) {
                    every { getInt() } returns 1337
                })
    }

    @TestFactory
    fun `test mockk stubbing strategy with generic class`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(javaClass<Bla<String>>())
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(javaClass<Bla<String>>()) { bla ->
                        assertThat(bla.getValue()).isEqualTo("generics need to be handled explicitly")
                    }
                }
                .test(MockKStubbingStrategies.mockk<Bla<String>> {
                    every { getValue() } returns "generics need to be handled explicitly"
                })
    }

    internal interface Foo {

        fun getInt(): Int

        fun getString(): String

    }

    internal interface Baz

    internal interface Bla<T : Any> {

        fun getValue(): T

    }

    internal enum class Qux

}