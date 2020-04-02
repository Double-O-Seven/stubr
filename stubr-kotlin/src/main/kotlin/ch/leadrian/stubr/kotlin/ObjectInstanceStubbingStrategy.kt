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

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingException
import ch.leadrian.stubr.core.strategy.SimpleStubbingStrategy
import java.lang.reflect.ParameterizedType

internal object ObjectInstanceStubbingStrategy : SimpleStubbingStrategy<Any>() {

    override fun acceptsClass(context: StubbingContext, type: Class<*>): Boolean {
        return type.kotlin.objectInstance != null
    }

    override fun acceptsParameterizedType(context: StubbingContext, type: ParameterizedType): Boolean {
        return accepts(context, type.rawType)
    }

    override fun stubClass(context: StubbingContext, type: Class<*>): Any {
        return type.kotlin.objectInstance ?: throw StubbingException(context.site, type)
    }

    override fun stubParameterizedType(context: StubbingContext, type: ParameterizedType): Any {
        return stub(context, type.rawType)
    }

}