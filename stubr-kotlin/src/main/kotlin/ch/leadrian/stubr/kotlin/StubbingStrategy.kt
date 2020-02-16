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