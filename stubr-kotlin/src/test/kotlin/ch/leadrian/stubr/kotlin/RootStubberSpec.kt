package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.Result
import ch.leadrian.stubr.core.Stubber
import ch.leadrian.stubr.core.stubbingsite.StubbingSites
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object RootStubberSpec : Spek({
    val stubber by memoized {
        Stubber.builder()
                .stubWith(KotlinStubbingStrategies.constantValue(1337))
                .build()
    }

    describe("tryToStub") {

        context("given site") {

            context("given stubbable type") {

                it("should return success") {
                    val value = stubber.tryToStub<Int>(StubbingSites.unknown())
                    assertThat(value)
                            .isEqualTo(Result.success(1337))
                }

            }

            context("given unknown type") {

                it("should return failure") {
                    val value = stubber.tryToStub<Long>(StubbingSites.unknown())
                    assertThat(value)
                            .isEqualTo(Result.failure<Long>())
                }

            }

        }

        context("given no site") {

            context("given stubbable type") {

                it("should return success") {
                    val value = stubber.tryToStub<Int>()
                    assertThat(value)
                            .isEqualTo(Result.success(1337))
                }

            }

            context("given unknown type") {

                it("should return failure") {
                    val value = stubber.tryToStub<Long>()
                    assertThat(value)
                            .isEqualTo(Result.failure<Long>())
                }

            }

        }

    }

    describe("stub") {

        context("given site") {

            it("should return stub value") {
                val value: Int = stubber.stub(StubbingSites.unknown())
                assertThat(value)
                        .isEqualTo(1337)
            }

        }

        context("given no site") {

            it("should return stub value") {
                val value: Int = stubber.stub()
                assertThat(value)
                        .isEqualTo(1337)
            }

        }

    }

})
