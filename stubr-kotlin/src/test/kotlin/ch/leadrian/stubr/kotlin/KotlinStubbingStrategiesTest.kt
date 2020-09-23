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

import ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class KotlinStubbingStrategiesTest {

    @TestFactory
    fun `test collection`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub("foo")
                .accepts(typeLiteral<ArrayList<String>>())
                .andStubs(ArrayList(listOf("foo", "foo", "foo")))
                .test(
                        KotlinStubbingStrategies.collection(3) { ArrayList(it) },
                        KotlinStubbingStrategies.collection({ ArrayList(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test map`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub(Int::class.javaObjectType, 1, 2, 3)
                .provideStub(String::class.java, "foo", "bar", "baz")
                .accepts(typeLiteral<HashMap<Int, String>>())
                .andStubs(HashMap(mapOf(1 to "foo", 2 to "bar", 3 to "baz")))
                .test(
                        KotlinStubbingStrategies.map(3) { HashMap(it) },
                        KotlinStubbingStrategies.map({ HashMap(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test primaryConstructor`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub(String::class.java, "primary")
                .accepts(PrimaryConstructorData::class.java)
                .andStubs(PrimaryConstructorData("primary"))
                .rejects(JavaPrimaryConstructorTestData::class.java)
                .test(KotlinStubbingStrategies.primaryConstructor())
    }

    @TestFactory
    fun `test suppliedValue`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo"))
                .test(
                        KotlinStubbingStrategies.suppliedValue { _, _ -> listOf("foo") },
                        KotlinStubbingStrategies.suppliedValue { _ -> listOf("foo") }
                )
    }

    @TestFactory
    fun `test implementation`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub(typeLiteral<List<String>>(), listOf("foo"))
                .accepts(typeLiteral<Collection<CharSequence>>())
                .andStubs(listOf("foo"))
                .test(KotlinStubbingStrategies.implementation<Collection<CharSequence>, List<String>>())
    }

    data class PrimaryConstructorData(val value: String) {

        constructor() : this("secondary")

    }

}