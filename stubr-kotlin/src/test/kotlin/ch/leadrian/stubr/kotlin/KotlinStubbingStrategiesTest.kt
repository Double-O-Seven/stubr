package ch.leadrian.stubr.kotlin

import ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

internal class KotlinStubbingStrategiesTest {

    @TestFactory
    fun `test collection`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub("foo")
                .accepts(typeLiteral<ArrayList<String>>())
                .andStubs(ArrayList(listOf("foo", "foo", "foo")))
                .test(
                        KotlinStubbingStrategies.collection(3) { ArrayList(it) },
                        KotlinStubbingStrategies.collection({ ArrayList(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test map`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub(Int::class.javaObjectType, 1, 2, 3)
                .provideStub(String::class.java, "foo", "bar", "baz")
                .accepts(typeLiteral<HashMap<Int, String>>())
                .andStubs(HashMap(mapOf(1 to "foo", 2 to "bar", 3 to "baz")))
                .test(
                        KotlinStubbingStrategies.map(3) { HashMap(it) },
                        KotlinStubbingStrategies.map({ HashMap(it) }) { 3 }
                )
    }

    @TestFactory
    fun `test suppliedValue`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .accepts(typeLiteral<List<String>>())
                .andStubs(listOf("foo"))
                .test(
                        KotlinStubbingStrategies.suppliedValue { _, _ -> listOf("foo") },
                        KotlinStubbingStrategies.suppliedValue { _ -> listOf("foo") }
                )
    }

    @TestFactory
    fun `test implementation`(): Stream<DynamicTest> {
        return stubbingStrategyTester()
                .provideStub(typeLiteral<List<String>>(), listOf("foo"))
                .accepts(typeLiteral<Collection<CharSequence>>())
                .andStubs(listOf("foo"))
                .test(KotlinStubbingStrategies.implementation<Collection<CharSequence>, List<String>>())
    }

}