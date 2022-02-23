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
import ch.leadrian.stubr.kotlin.KotlinStubbingStrategies
import ch.leadrian.stubr.kotlin.stub
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object SpekStubbersSpec : Spek({
    val stubber by useStubber {
        Stubber.builder()
            .stubWith(KotlinStubbingStrategies.suppliedValue { i -> "stub $i" })
            .build()
    }

    describe("stubs") {
        val stub1 by memoizedStub<String>()
        val stub2 by memoized<String> { stubber.stub() }
        val stub3 by memoizedStub<String> { uppercase() }
        val stub4 by memoized<String> { stubber.stub() }

        it("should provide and memoize stubs from stubber") {
            assertAll(
                { assertThat(stub1).isEqualTo("stub 0") },
                { assertThat(stub1).isEqualTo("stub 0") },
                { assertThat(stub2).isEqualTo("stub 1") },
                { assertThat(stub2).isEqualTo("stub 1") },
                { assertThat(stub3).isEqualTo("STUB 2") },
                { assertThat(stub3).isEqualTo("STUB 2") },
                { assertThat(stub4).isEqualTo("stub 3") },
                { assertThat(stub4).isEqualTo("stub 3") },
            )
        }
    }
})