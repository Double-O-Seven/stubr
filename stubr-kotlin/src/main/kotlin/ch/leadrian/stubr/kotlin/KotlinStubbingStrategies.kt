/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.strategy.StubbingStrategies
import java.lang.reflect.Constructor
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.kotlinFunction

/**
 * Collection of factory methods for [StubbingStrategy] to allow simplified use of Stubr in Kotlin.
 */
@Suppress("UNCHECKED_CAST")
object KotlinStubbingStrategies {

    /**
     * Returns a [StubbingStrategy] that stubs Kotlin objects.
     *
     * @return a [StubbingStrategy] that stubs Kotlin objects
     */
    @JvmStatic
    fun objectInstance(): StubbingStrategy = ObjectInstanceStubbingStrategy

    /**
     * Inlined wrapper for [StubbingStrategies.constantValue].
     *
     * @param T reified type of the constant [value]
     * @return a [StubbingStrategy] using the given value every time the type of the given [value] is encountered
     */
    inline fun <reified T> constantValue(value: T): StubbingStrategy =
        StubbingStrategies.constantValue(typeLiteral<T>(), value)

    /**
     * Inlined wrapper for [StubbingStrategies.collection].
     *
     * @param collectionFactory a factory providing a concrete instance of the collection class
     * @param collectionSize the provided collection size
     * @param T reified type of the collection
     * @return a [StubbingStrategy] for stubbing collections
     */
    inline fun <reified T : Collection<*>> collection(
        crossinline collectionFactory: (List<*>) -> T,
        crossinline collectionSize: (StubbingContext) -> Int
    ): StubbingStrategy =
        StubbingStrategies.collection(T::class.java, { collectionFactory(it) }, { collectionSize(it) })

    /**
     * Inlined wrapper for [StubbingStrategies.collection].
     *
     * @param collectionFactory a factory providing a concrete instance of the collection class
     * @param collectionSize the collection size
     * @param T reified type of the collection
     * @return a [StubbingStrategy] for stubbing collections
     */
    inline fun <reified T : Collection<*>> collection(
        collectionSize: Int,
        crossinline collectionFactory: (List<*>) -> T
    ): StubbingStrategy = collection(collectionFactory) { collectionSize }

    /**
     * Inlined wrapper for [StubbingStrategies.map].
     *
     * @param mapFactory a factory providing a concrete instance of the map class
     * @param mapSize the provided map size
     * @param T reified type of the map
     * @return a [StubbingStrategy] for stubbing maps
     */
    inline fun <reified T : Map<*, *>> map(
        crossinline mapFactory: (Map<*, *>) -> T,
        crossinline mapSize: (StubbingContext) -> Int
    ): StubbingStrategy = StubbingStrategies.map(T::class.java, { mapFactory(it) }, { mapSize(it) })

    /**
     * Inlined wrapper for [StubbingStrategies.map].
     *
     * @param mapFactory a factory providing a concrete instance of the map class
     * @param mapSize the map size
     * @param T reified type of the map
     * @return a [StubbingStrategy] for stubbing maps
     */
    inline fun <reified T : Map<*, *>> map(
        mapSize: Int,
        crossinline mapFactory: (Map<*, *>) -> T
    ): StubbingStrategy = map(mapFactory) { mapSize }

    /**
     * Returns a stubbing strategy that uses the primary Kotlin constructor to instantiate an object.
     *
     * @return a [StubbingStrategy] that uses the primary Kotlin constructor
     */
    @JvmStatic
    fun primaryConstructor(): StubbingStrategy {
        return StubbingStrategies.constructor { _, constructor: Constructor<*> ->
            constructor.kotlinFunction == constructor.declaringClass.kotlin.primaryConstructor
        }
    }

    /**
     * Inlined wrapper for [StubbingStrategies.suppliedValue].
     *
     * @param supplier the value supplying function
     * @param T reified type of the supplied value
     * @return a [StubbingStrategy] providing a stub values using a supplying function
     */
    inline fun <reified T> suppliedValue(crossinline supplier: (StubbingContext, Int) -> T): StubbingStrategy {
        return StubbingStrategies.suppliedValue(typeLiteral<T>()) { context, sequenceNumber ->
            supplier(context, sequenceNumber)
        }
    }

    /**
     * Inlined wrapper for [StubbingStrategies.suppliedValue].
     *
     * @param supplier the value supplying function
     * @param T reified type of the supplied value
     * @return a [StubbingStrategy] providing a stub values using a supplying function
     */
    inline fun <reified T> suppliedValue(crossinline supplier: (Int) -> T): StubbingStrategy =
        StubbingStrategies.suppliedValue(typeLiteral<T>()) { sequenceNumber -> supplier(sequenceNumber) }

    /**
     * Inlined wrapper for [StubbingStrategies.implementation].
     *
     * @param T reified type to be stubbed
     * @param U reified implementation type
     * @return a [StubbingStrategy] that delegates the stubbing of an instance of [T] to stubbing a value for [U]
     */
    inline fun <reified T, reified U : T> implementation(): StubbingStrategy =
        StubbingStrategies.implementation(typeLiteral<T>(), typeLiteral<U>())

}