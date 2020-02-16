package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Matcher

/**
 * Collection of Kotlin [Matcher]s.
 */
object KotlinMatchers {

    /**
     * Returns a [Matcher] matches if the stubbing site is a method parameter, constructor parameter or method return
     * where the type is nullable.
     *
     * @return a [Matcher] matching if the Kotlin type at a site is nullable
     */
    @JvmStatic
    fun <T> kotlinTypeIsNullable(): Matcher<T> = KotlinTypeIsNullableMatcher()

}