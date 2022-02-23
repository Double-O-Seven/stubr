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

package ch.leadrian.stubr.spek

import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.type.TypeLiteral
import ch.leadrian.stubr.kotlin.typeLiteral
import org.spekframework.spek2.dsl.LifecycleAware
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.lifecycle.MemoizedValue

/**
 * Returns a [MemoizedValue] containing the stubber provided by [stubberFactory].
 *
 * The created stubber my also be access using a property named "stubber" delegating to a parameterless call
 * of [LifecycleAware.memoized].
 *
 * @param mode the [CachingMode] pass to [LifecycleAware.memoized]
 * @param stubberFactory the function used for instantiating the [Stubber]
 * @return a [MemoizedValue] containing the stubber provided by [stubberFactory]
 */
fun LifecycleAware.useStubber(
    mode: CachingMode = defaultCachingMode,
    stubberFactory: () -> Stubber,
): MemoizedValue<Stubber> {
    val stubber by memoized(mode, stubberFactory)
    return memoized(mode) { stubber }
}

/**
 * Returns a stub memoized using [LifecycleAware.memoized] provided my the stubber of the current test.
 *
 * In order for this call to succeed, a [Stubber] must be configured using [useStubber].
 *
 * An optional block [block] may be passed to this function in order to modify or even copy or replace the provided
 * stub. The lambda is executed in the scope of the given stub of type [T] and the used [Stubber] is passed as the
 * non-receiver parameter.
 *
 * @param T the type of the memoized stub value
 * @param mode the [CachingMode] pass to [LifecycleAware.memoized]
 * @param site the site of the memoized stub, defaults to [MemoizingStubbingSite]
 * @param block a lambda function that allows to further configure the stubbed value
 * @return a stub memoized using [LifecycleAware.memoized] provided my the stubber
 */
inline fun <reified T : Any> LifecycleAware.memoizedStub(
    mode: CachingMode = defaultCachingMode,
    site: StubbingSite = MemoizingStubbingSite,
    noinline block: T.(Stubber) -> T = { this },
): MemoizedValue<T> = memoizedStub(typeLiteral(), mode, site, block)

fun <T : Any> LifecycleAware.memoizedStub(
    typeLiteral: TypeLiteral<T>,
    mode: CachingMode = defaultCachingMode,
    site: StubbingSite = MemoizingStubbingSite,
    block: T.(Stubber) -> T = { this },
): MemoizedValue<T> {
    val stubber by memoized<Stubber>()
    return memoized(mode) {
        stubber.stub(typeLiteral, site).run { block(stubber) }
    }
}