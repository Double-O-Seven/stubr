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

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingSite
import ch.leadrian.stubr.core.type.TypeLiteral
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Inlined variant of [Stubber.tryToStub] for stubbing a value of type [T].
 *
 * @return a stub value of type [T]
 * @see Stubber.tryToStub
 */
inline fun <reified T> Stubber.tryToStub(site: StubbingSite? = null): Result<T> {
    val typeLiteral = typeLiteral<T>()
    return if (site != null) {
        tryToStub(typeLiteral, site)
    } else {
        tryToStub(typeLiteral)
    }
}

/**
 * Inlined variant of [Stubber.stub] for stubbing a value of type [T].
 *
 * @return a stub value of type [T]
 * @see Stubber.stub
 */
inline fun <reified T> Stubber.stub(site: StubbingSite? = null): T {
    val typeLiteral = typeLiteral<T>()
    return if (site != null) {
        stub(typeLiteral, site)
    } else {
        stub(typeLiteral)
    }
}

/**
 * Property delegation function that provides a new stub whenever the property is read.
 *
 * @return new stub of type [T]
 */
inline operator fun <reified T> Stubber.getValue(thisRef: Any?, property: KProperty<*>): T {
    val site = KPropertyStubbingSite(thisRef, property)
    return stub(typeLiteral(), site)
}

/**
 * Property delegate that provides a stub value for the given property. The returned property may be reused since the
 * stub is associated with the given `this` and the property itself.
 *
 * The stub will be stubbed at a [KPropertyStubbingSite].
 *
 * @return a memoized stub of type [T], associated with the property owner (`this`) and the property
 * @see KPropertyStubbingSite
 */
inline fun <reified T> Stubber.stubbing(): ReadOnlyProperty<Any?, T> =
        StubProperty(this, typeLiteral())

@PublishedApi
internal class StubProperty<in R, T>(
        private val stubber: Stubber,
        private val typeLiteral: TypeLiteral<T>
) : ReadOnlyProperty<R, T> {

    private val stubValuesBySite = ConcurrentHashMap<StubbingSite, T>()

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        val site = KPropertyStubbingSite(thisRef, property)
        return stubValuesBySite.computeIfAbsent(site) { stubber.stub(typeLiteral, it) }
    }

}