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

internal class GenericMockStubbingStrategy(
        private val relaxed: Boolean = false,
        private val relaxUnitFun: Boolean = false,
        private val moreInterfaces: Array<out KClass<*>>,
        private val block: Any.() -> Unit = {}
) : SimpleStubbingStrategy<Any>() {

    override fun acceptsClass(context: StubbingContext, type: Class<*>): Boolean =
            !(type.isEnum || type.isArray || type.isPrimitive)

    override fun acceptsParameterizedType(context: StubbingContext, type: ParameterizedType): Boolean =
            accepts(context, type.rawType)

    override fun stubClass(context: StubbingContext, type: Class<*>): Any {
        return mockkClass(
                type = type.kotlin,
                relaxed = relaxed,
                relaxUnitFun = relaxUnitFun,
                moreInterfaces = *moreInterfaces,
                block = block
        )
    }

    override fun stubParameterizedType(context: StubbingContext, type: ParameterizedType): Any =
            stub(context, type.rawType)

}