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

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.strategy.SimpleStubbingStrategy
import io.mockk.mockkClass
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

internal class MockStubbingStrategy<T : Any>(
        private val type: KClass<T>,
        private val relaxed: Boolean,
        private val relaxUnitFun: Boolean,
        private val moreInterfaces: Array<out KClass<*>>,
        private val block: T.(StubbingContext) -> Unit
) : SimpleStubbingStrategy<T>() {

    override fun acceptsClass(context: StubbingContext, type: Class<*>): Boolean = type == this.type.java

    override fun acceptsParameterizedType(context: StubbingContext, type: ParameterizedType): Boolean =
            type == this.type.java

    override fun stubClass(context: StubbingContext, type: Class<*>): T = createMock { block(context) }

    override fun stubParameterizedType(context: StubbingContext, type: ParameterizedType): T =
            createMock { block(context) }

    private inline fun createMock(block: T.() -> Unit): T {
        return mockkClass(
                type = type,
                relaxed = relaxed,
                relaxUnitFun = relaxUnitFun,
                moreInterfaces = *moreInterfaces,
                block = block
        )
    }

}