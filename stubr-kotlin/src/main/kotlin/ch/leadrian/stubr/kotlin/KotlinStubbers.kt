package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.stubber.Stubbers
import java.util.function.IntFunction

@Suppress("UNCHECKED_CAST")
object KotlinStubbers {

    @JvmStatic
    fun objectInstance(): Stubber = ObjectInstanceStubber

    inline fun <reified T> constantValue(value: T): Stubber = Stubbers.constantValue(typeLiteral<T>(), value)

    inline fun <reified T : Collection<*>> collection(
            crossinline collectionFactory: (List<*>) -> T,
            crossinline collectionSize: (StubbingContext) -> Int
    ): Stubber = Stubbers.collection(T::class.java, { collectionFactory(it) }, { collectionSize(it) })

    inline fun <reified T : Collection<*>> collection(
            collectionSize: Int,
            crossinline collectionFactory: (List<*>) -> T
    ): Stubber = collection(collectionFactory) { collectionSize }

    inline fun <reified T : Map<*, *>> map(
            crossinline mapFactory: (Map<*, *>) -> T,
            crossinline mapSize: (StubbingContext) -> Int
    ): Stubber = Stubbers.map(T::class.java, { mapFactory(it) }, { mapSize(it) })

    inline fun <reified T : Map<*, *>> map(
            mapSize: Int,
            crossinline mapFactory: (Map<*, *>) -> T
    ): Stubber = map(mapFactory) { mapSize }

    inline fun <reified T> suppliedValue(crossinline supplier: (Int) -> T): Stubber =
            Stubbers.suppliedValue(typeLiteral<T>(), IntFunction { supplier(it) })

}