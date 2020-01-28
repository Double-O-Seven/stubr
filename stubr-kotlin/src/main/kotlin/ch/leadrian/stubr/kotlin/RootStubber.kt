package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.RootStubber
import ch.leadrian.stubr.core.StubbingSite

inline fun <reified T> RootStubber.tryToStub(site: StubbingSite? = null): Result<T> {
    val typeLiteral = typeLiteral<T>()
    return if (site != null) {
        tryToStub(typeLiteral, site)
    } else {
        tryToStub(typeLiteral)
    }
}

inline fun <reified T> RootStubber.stub(site: StubbingSite? = null): T {
    val typeLiteral = typeLiteral<T>()
    return if (site != null) {
        stub(typeLiteral, site)
    } else {
        stub(typeLiteral)
    }
}