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
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.matcher.Matchers
import ch.leadrian.stubr.core.matcher.Matchers.mappedTo
import java.util.function.Function
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Collection of Kotlin [Matcher]s.
 */
object KotlinMatchers {

    /**
     * Returns a [Matcher] that matches if the stubbing site is a method parameter, constructor parameter or method return
     * where the type is nullable.
     *
     * @return a [Matcher] matching if the Kotlin type at a site is nullable
     */
    @JvmStatic
    fun <T : StubbingSite> kotlinTypeNullable(): Matcher<T> = KotlinTypeNullableMatcher()

    /**
     * `KClass`-based variant of [Matchers.instanceOf].
     */
    @JvmStatic
    fun <T, U : Any> instanceOf(targetClass: KClass<U>, delegate: Matcher<in U>? = null): Matcher<T> =
            Matchers.instanceOf(targetClass.java, delegate)

    /**
     * `KClass`-based variant of [Matchers.instanceOf].
     */
    inline fun <T, U : Any> instanceOf(
            targetClass: KClass<U>,
            crossinline delegate: (StubbingContext, U) -> Boolean
    ): Matcher<T> = instanceOf(targetClass, Matcher { context, value -> delegate(context, value) })

    /**
     * Inlined variant of [Matchers.instanceOf].
     */
    inline fun <T, reified U : Any> instanceOf(delegate: Matcher<in U>? = null): Matcher<T> =
            instanceOf(U::class, delegate)

    /**
     * Inlined variant of [Matchers.instanceOf].
     */
    inline fun <T, reified U : Any> instanceOf(crossinline delegate: (StubbingContext, U) -> Boolean): Matcher<T> =
            instanceOf(U::class, delegate)

    /**
     * Returns a [Matcher] that matches if the stubbing site is a [KPropertyStubbingSite] and the [delegate] matches it.
     *
     * @return a [Matcher] matching if the stubbing site is a [KPropertyStubbingSite] and the [delegate] matches it
     */
    @JvmStatic
    fun <T : StubbingSite> kotlinProperty(delegate: Matcher<in KProperty<*>>): Matcher<T> {
        return instanceOf(
                KPropertyStubbingSite::class,
                mappedTo(Function { site: KPropertyStubbingSite -> site.property }, delegate)
        )
    }

    /**
     * Inlined variant of [kotlinProperty].
     */
    inline fun <T : StubbingSite> kotlinProperty(crossinline delegate: (StubbingContext, KProperty<*>) -> Boolean): Matcher<T> =
            kotlinProperty(Matcher { context, value -> delegate(context, value) })

}