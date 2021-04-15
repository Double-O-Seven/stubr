/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.site.ConstructorParameterStubbingSite
import ch.leadrian.stubr.core.site.MethodParameterStubbingSite
import ch.leadrian.stubr.core.site.MethodReturnValueStubbingSite
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

internal class KotlinTypeNullableMatcher<T : StubbingSite> : Matcher<T> {

    override fun matches(context: StubbingContext, value: T): Boolean {
        return when (value) {
            is MethodParameterStubbingSite      -> isParameterTypeNullable(value)
            is MethodReturnValueStubbingSite    -> isReturnTypeNullable(value)
            is ConstructorParameterStubbingSite -> isParameterTypeNullable(value)
            else                                -> false
        }
    }

    private fun isParameterTypeNullable(site: MethodParameterStubbingSite): Boolean {
        val function = site.method.kotlinFunction ?: return false
        // Kotlin function might have an offset if there's a receiver
        val parameterOffset = function.parameters.size - site.method.parameterCount
        val index = site.parameterIndex + parameterOffset
        return function.isParameterTypeNullable(index)
    }

    private fun isReturnTypeNullable(site: MethodReturnValueStubbingSite): Boolean =
            site.method.kotlinFunction?.returnType?.isMarkedNullable ?: false

    private fun isParameterTypeNullable(site: ConstructorParameterStubbingSite): Boolean {
        val function = site.constructor.kotlinFunction ?: return false
        return function.isParameterTypeNullable(site.parameterIndex)
    }

    private fun KFunction<*>.isParameterTypeNullable(index: Int): Boolean = parameters[index].type.isMarkedNullable
}