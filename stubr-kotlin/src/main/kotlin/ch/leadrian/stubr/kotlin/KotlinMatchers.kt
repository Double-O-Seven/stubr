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
    fun <T> kotlinTypeIsNullable(): Matcher<T> = KotlinTypeIsNullableMatcher()

    /**
     * Returns a [Matcher] that matches if the stubbing site is a [KPropertyStubbingSite] and the [delegate] matches it.
     *
     * @return a [Matcher] matching if the stubbing site is a [KPropertyStubbingSite] and the [delegate] matches it
     */
    @JvmStatic
    fun <T> kotlinPropertyIs(delegate: Matcher<in KProperty<*>>): Matcher<T> = KPropertyMatcher(delegate)

    inline fun <T> kotlinPropertyIs(crossinline delegate: (StubbingContext, KProperty<*>) -> Boolean): Matcher<T> {
        val delegateMatcher = Matcher<KProperty<*>> { context, value -> delegate(context, value) }
        return kotlinPropertyIs(delegateMatcher)
    }

}