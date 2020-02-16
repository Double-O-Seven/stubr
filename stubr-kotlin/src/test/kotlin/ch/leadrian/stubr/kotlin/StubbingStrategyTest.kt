package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.StubbingContext
import ch.leadrian.stubr.core.StubbingStrategy
import ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.lang.reflect.Type
import java.util.stream.Stream

internal class StubbingStrategyTest {

    @TestFactory
    fun testApplyWhen(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(String::class.java)
                .accepts(Int::class.java)
                .rejects(Float::class.java)
                .rejects(Any::class.java)
                .test(NullStubbingStrategy.applyWhen { _, type -> type == String::class.java || type == Int::class.java })
    }

    private object NullStubbingStrategy : StubbingStrategy {

        override fun accepts(context: StubbingContext, type: Type): Boolean = true

        override fun stub(context: StubbingContext, type: Type): Any? = null

    }

}