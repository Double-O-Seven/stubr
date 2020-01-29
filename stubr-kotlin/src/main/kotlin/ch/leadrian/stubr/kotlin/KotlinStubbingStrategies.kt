package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.strategy.StubbingStrategies
import java.util.function.IntFunction

@Suppress("UNCHECKED_CAST")
object KotlinStubbingStrategies {

    @JvmStatic
    fun objectInstance(): StubbingStrategy = ObjectInstanceStubbingStrategy

    inline fun <reified T> constantValue(value: T): StubbingStrategy = StubbingStrategies.constantValue(typeLiteral<T>(), value)

    inline fun <reified T : Collection<*>> collection(
            crossinline collectionFactory: (List<*>) -> T,
            crossinline collectionSize: (StubbingContext) -> Int
    ): StubbingStrategy = StubbingStrategies.collection(T::class.java, { collectionFactory(it) }, { collectionSize(it) })

    inline fun <reified T : Collection<*>> collection(
            collectionSize: Int,
            crossinline collectionFactory: (List<*>) -> T
    ): StubbingStrategy = collection(collectionFactory) { collectionSize }

    inline fun <reified T : Map<*, *>> map(
            crossinline mapFactory: (Map<*, *>) -> T,
            crossinline mapSize: (StubbingContext) -> Int
    ): StubbingStrategy = StubbingStrategies.map(T::class.java, { mapFactory(it) }, { mapSize(it) })

    inline fun <reified T : Map<*, *>> map(
            mapSize: Int,
            crossinline mapFactory: (Map<*, *>) -> T
    ): StubbingStrategy = map(mapFactory) { mapSize }

    inline fun <reified T> suppliedValue(crossinline supplier: (Int) -> T): StubbingStrategy =
            StubbingStrategies.suppliedValue(typeLiteral<T>(), IntFunction { supplier(it) })

    inline fun <reified T, reified U : T> implementation(): StubbingStrategy = StubbingStrategies.implementation(typeLiteral<T>(), typeLiteral<U>())

}