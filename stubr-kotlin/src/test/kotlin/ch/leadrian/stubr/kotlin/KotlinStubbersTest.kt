package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.testing.StubberTester.stubberTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class KotlinStubbersTest {

    @TestFactory
    fun `test collection`(): Stream<DynamicTest> {
        return stubberTester()
                .provideStub("foo")
                .accepts(typeLiteral<ArrayList<String>>())
                .andStubs(ArrayList(listOf("foo", "foo", "foo")))
                .test(
                        KotlinStubbers.collection(3) { ArrayList(it) },
                        KotlinStubbers.collection({ ArrayList(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test map`(): Stream<DynamicTest> {
        return stubberTester()
                .provideStub(Int::class.javaObjectType, 1, 2, 3)
                .provideStub(String::class.java, "foo", "bar", "baz")
                .accepts(typeLiteral<HashMap<Int, String>>())
                .andStubs(HashMap(mapOf(1 to "foo", 2 to "bar", 3 to "baz")))
                .test(
                        KotlinStubbers.map(3) { HashMap(it) },
                        KotlinStubbers.map({ HashMap(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test suppliedValue`(): Stream<DynamicTest> {
        return stubberTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo"))
                .test(KotlinStubbers.suppliedValue { listOf("foo") })
    }

    @TestFactory
    fun `test implementation`(): Stream<DynamicTest> {
        return stubberTester()
                .provideStub(typeLiteral<List<String>>(), listOf("foo"))
                .accepts(typeLiteral<Collection<CharSequence>>())
                .andStubs(listOf("foo"))
                .test(KotlinStubbers.implementation<Collection<CharSequence>, List<String>>())
    }

}