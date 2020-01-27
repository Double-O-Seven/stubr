package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.testing.StubberTester.stubberTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class ObjectInstanceStubberTest {

    @TestFactory
    fun `test ObjectInstanceStubber`(): Stream<DynamicTest> {
        return stubberTester()
                .accepts(TestObject::class.java)
                .andStubs(TestObject)
                .rejects(TestClass::class.java)
                .test(KotlinStubbers.objectInstance())
    }

    @TestFactory
    fun `test constantValue`(): Stream<DynamicTest> {
        return stubberTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo", "bar"))
                .rejects(typeLiteral<List<Int>>())
                .test(KotlinStubbers.constantValue(listOf("foo", "bar")))
    }

    object TestObject

    class TestClass

}