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

package ch.leadrian.stubr.mockk

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingException
import ch.leadrian.stubr.core.strategy.SimpleStubbingStrategy
import ch.leadrian.stubr.core.type.Types
import io.mockk.mockkClass
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

internal class GenericMockStubbingStrategy(
    private val relaxed: Boolean,
    private val relaxUnitFun: Boolean,
    private val moreInterfaces: Array<out KClass<*>>,
    private val block: Any.(StubbingContext, Type) -> Unit
) : SimpleStubbingStrategy<Any>() {

    override fun acceptsClass(context: StubbingContext, type: Class<*>): Boolean =
        !(type.isEnum || type.isArray || type.isPrimitive)

    override fun acceptsParameterizedType(context: StubbingContext, type: ParameterizedType): Boolean =
        accepts(context, type.rawType)

    override fun acceptsGenericArrayType(context: StubbingContext, type: GenericArrayType): Boolean = false

    override fun stubClass(context: StubbingContext, type: Class<*>): Any = createMock(type) { block(context, type) }

    override fun stubParameterizedType(context: StubbingContext, type: ParameterizedType): Any {
        val rawType = Types.getRawType(type).orElseThrow { StubbingException(context.site, type) }
        return createMock(rawType) { block(context, type) }
    }

    override fun stubGenericArrayType(context: StubbingContext, type: GenericArrayType): Any {
        throw StubbingException(context.site, type)
    }

    private inline fun createMock(type: Class<*>, block: Any.() -> Unit): Any {
        return mockkClass(
            type = type.kotlin,
            relaxed = relaxed,
            relaxUnitFun = relaxUnitFun,
            block = block,
            moreInterfaces = moreInterfaces,
        )
    }

}