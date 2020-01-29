package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class ObjectInstanceStubbingStrategyTest {

    @TestFactory
    fun `test ObjectInstanceStubber`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(TestObject::class.java)
                .andStubs(TestObject)
                .rejects(TestClass::class.java)
                .test(KotlinStubbingStrategies.objectInstance())
    }

    @TestFactory
    fun `test constantValue`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo", "bar"))
                .rejects(typeLiteral<List<Int>>())
                .test(KotlinStubbingStrategies.constantValue(listOf("foo", "bar")))
    }

    object TestObject

    class TestClass

}