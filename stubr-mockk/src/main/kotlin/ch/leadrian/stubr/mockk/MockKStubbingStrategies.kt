/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
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

package ch.leadrian.stubr.mockk

import ch.leadrian.stubr.core.StubbingStrategy
import kotlin.reflect.KClass

/**
 * Collection of factory methods to create [ch.leadrian.stubr.core.Stubber]s that use MockK.
 */
object MockKStubbingStrategies {

    /**
     * Returns a [ch.leadrian.stubr.core.StubbingStrategy] that creates stubs using MockK's `mockkClass` function.
     * The accepted parameters are identical to the ones found in `mockkClass` with the only difference that the default
     * value of [relaxed] is `true` instead of `false`.
     *
     * The stubbing strategy accepts any type that is not an array, enum or primitive. So careful configuration will be
     * required.
     */
    @JvmStatic
    @JvmOverloads
    fun mockkAny(
            relaxed: Boolean = true,
            relaxUnitFun: Boolean = false,
            vararg moreInterfaces: KClass<*>,
            block: Any.() -> Unit = {}
    ): StubbingStrategy {
        return GenericMockStubbingStrategy(
                relaxed = relaxed,
                relaxUnitFun = relaxUnitFun,
                moreInterfaces = moreInterfaces,
                block = block
        )
    }

    /**
     * Returns a [ch.leadrian.stubr.core.StubbingStrategy] that creates stubs using MockK's `mockkClass` function for a
     * specific type [T].
     * The accepted parameters are identical to the ones found in `mockkClass` with the only difference that the default
     * value of [relaxed] is `true` instead of `false`.
     *
     * Note that MockK's `mockk` is not used here since this function accepts only a reified type parameter.
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Any> mockk(
            type: KClass<T>,
            relaxed: Boolean = true,
            relaxUnitFun: Boolean = false,
            vararg moreInterfaces: KClass<*>,
            block: T.() -> Unit = {}
    ): StubbingStrategy {
        return MockStubbingStrategy(
                type = type,
                relaxed = relaxed,
                relaxUnitFun = relaxUnitFun,
                moreInterfaces = moreInterfaces,
                block = block
        )
    }

    /**
     * Inlined variant of [mockk] that uses a reified type [T].
     */
    inline fun <reified T : Any> mockk(
            relaxed: Boolean = true,
            relaxUnitFun: Boolean = false,
            vararg moreInterfaces: KClass<*>,
            noinline block: T.() -> Unit = {}
    ): StubbingStrategy {
        return mockk(
                type = T::class,
                relaxed = relaxed,
                relaxUnitFun = relaxUnitFun,
                moreInterfaces = *moreInterfaces,
                block = block
        )
    }

}