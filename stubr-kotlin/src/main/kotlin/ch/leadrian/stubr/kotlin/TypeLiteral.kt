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

import ch.leadrian.stubr.core.type.TypeLiteral

/**
 * Inlined function for creating a type literal of a reified type [T].
 *
 * @param T the reified type
 * @return a type literal for type [T]
 * @see TypeLiteral
 */
inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {}
