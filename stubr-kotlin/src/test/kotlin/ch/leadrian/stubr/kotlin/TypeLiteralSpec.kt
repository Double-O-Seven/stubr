package ch.leadrian.stubr.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object TypeLiteralSpec : Spek({

    describe("typeLiteral") {
        val typeLiteral = typeLiteral<String>()

        it("should be created with parameterized type") {
            assertThat(typeLiteral.type)
                    .isEqualTo(String::class.java)
        }
    }

})