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

import ch.leadrian.stubr.core.Matcher
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.lang.reflect.Type
import java.util.stream.Stream

internal class StubbingStrategyTest {

    @TestFactory
    fun testApplyWhen(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(String::class.java)
                .accepts(Int::class.java)
                .rejects(Float::class.java)
                .rejects(Any::class.java)
                .test(
                        NullStubbingStrategy.applyWhen(Matcher { _, type -> type == String::class.java || type == Int::class.java }),
                        NullStubbingStrategy.applyWhen { _, type -> type == String::class.java || type == Int::class.java }
                )
    }

    private object NullStubbingStrategy : StubbingStrategy {

        override fun accepts(context: StubbingContext, type: Type): Boolean = true

        override fun stub(context: StubbingContext, type: Type): Any? = null

    }

}