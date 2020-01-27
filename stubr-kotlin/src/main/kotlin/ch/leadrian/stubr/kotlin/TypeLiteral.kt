package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.type.TypeLiteral

inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {}
