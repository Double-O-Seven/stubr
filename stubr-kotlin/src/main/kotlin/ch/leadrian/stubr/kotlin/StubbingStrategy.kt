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
import java.lang.reflect.Type

/**
 * Wrapper for [StubbingStrategy.when].
 *
 * This wrapper avoids having to use back ticks when invoking [StubbingStrategy.when] from Kotlin.
 *
 * @return the matcher used to match a given [Type]
 * @return a new [StubbingStrategy] that is only applied when the given [matcher] matches
 */
fun StubbingStrategy.applyWhen(matcher: Matcher<in Type>): StubbingStrategy =
        this.`when`(matcher)

/**
 * Inline wrapper for [StubbingStrategy.when].
 *
 * This wrapper avoids having to use back ticks when invoking [StubbingStrategy.when] from Kotlin.
 *
 * @return the matcher used to match a given [Type]
 * @return a new [StubbingStrategy] that is only applied when the given [matcher] matches
 */
inline fun StubbingStrategy.applyWhen(crossinline matcher: (StubbingContext, Type) -> Boolean): StubbingStrategy =
        this.`when` { context, value -> matcher(context, value) }