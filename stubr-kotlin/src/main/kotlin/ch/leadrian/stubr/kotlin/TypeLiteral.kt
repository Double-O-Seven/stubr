package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.type.TypeLiteral

/**
 * Inlined function for creating a type literal of a reified type [T].
 *
 * @param T the reified type
 * @return a type literal for type [T]
 * @see TypeLiteral
 */
inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {}
