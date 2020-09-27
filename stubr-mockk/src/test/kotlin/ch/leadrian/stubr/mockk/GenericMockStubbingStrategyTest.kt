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
import ch.leadrian.stubr.core.type.TypeLiteral
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertAll
import java.util.stream.Stream

internal class GenericMockStubbingStrategyTest {

    @TestFactory
    fun `test mockk stubbing strategy`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(Foo::class.java)
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(javaClass<Foo>()) { foo ->
                        assertAll(
                                { assertThat(foo.getInt()).isZero() },
                                { assertThat(foo.getString()).isEmpty() }
                        )
                    }
                }
                .accepts(object : TypeLiteral<Bla<String>>() {})
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(javaClass<Bla<String>>()) { bla ->
                        assertAll(
                                { assertThat(bla.getInt()).isZero() },
                                { assertThat(bla.getString()).isEmpty() }
                        )
                    }
                }
                .accepts(Bar::class.java)
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(javaClass<Bar>()) { bar ->
                        assertAll(
                                { assertThat(bar.getInt()).isZero() },
                                { assertThat(bar.getString()).isEmpty() }
                        )
                    }
                }
                .accepts(OpenClass::class.java)
                .andStubSatisfies { stub: Any ->
                    assertThat(stub).isInstanceOfSatisfying(javaClass<OpenClass>()) { finalClass ->
                        assertAll(
                                { assertThat(finalClass.getInt()).isZero() },
                                { assertThat(finalClass.getString()).isEmpty() }
                        )
                    }
                }
                .rejects(Int::class.javaPrimitiveType)
                .rejects(Array<Any>::class.java)
                .rejects(Qux::class.java)
                .test(MockKStubbingStrategies.mockkAny())
    }

    internal interface Foo {

        fun getInt(): Int {
            throw UnsupportedOperationException()
        }

        fun getString(): String {
            throw UnsupportedOperationException()
        }
    }

    @Suppress("unused")
    internal interface Bla<T> {

        fun getInt(): Int {
            throw UnsupportedOperationException()
        }

        fun getString(): String {
            throw UnsupportedOperationException()
        }
    }

    internal class Bar {

        fun getInt(): Int {
            throw UnsupportedOperationException()
        }

        fun getString(): String {
            throw UnsupportedOperationException()
        }
    }

    internal open class OpenClass {

        fun getInt(): Int {
            throw UnsupportedOperationException()
        }

        fun getString(): String {
            throw UnsupportedOperationException()
        }
    }

    internal enum class Qux

}