package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.StubbingSite

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