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

import ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class ObjectInstanceStubbingStrategyTest {

    @TestFactory
    fun `test ObjectInstanceStubber`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(TestObject::class.java)
                .andStubs(TestObject)
                .rejects(TestClass::class.java)
                .test(KotlinStubbingStrategies.objectInstance())
    }

    @TestFactory
    fun `test constantValue`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo", "bar"))
                .rejects(typeLiteral<List<Int>>())
                .test(KotlinStubbingStrategies.constantValue(listOf("foo", "bar")))
    }

    object TestObject

    class TestClass

}