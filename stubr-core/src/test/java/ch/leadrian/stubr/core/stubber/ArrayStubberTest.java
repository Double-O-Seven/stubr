package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.testing.StubberTester;
import ch.leadrian.stubr.core.testing.TestStubbingSite;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

class ArrayStubberTest {

    @TestFactory
    Stream<DynamicTest> testEmptyArray() {
        return StubberTester.stubberTester()
                .accepts(Object[].class)
                .andStubs(new Object[0])
                .accepts(String[].class)
                .andStubs(new String[0])
                .rejects(Object.class)
                .rejects(String.class)
                .test(
                        Stubbers.array(context -> 0),
                        Stubbers.array(0),
                        Stubbers.array()
                );
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyArray() {
        return StubberTester.stubberTester()
                .provideStub(Object.class, 1, 2, 3)
                .provideStub(String.class, "Foo", "Bar", "Baz")
                .accepts(Object[].class)
                .andStubs(new Object[]{1, 2, 3})
                .at(
                        StubbingSites.array(TestStubbingSite.INSTANCE, Object.class),
                        StubbingSites.array(TestStubbingSite.INSTANCE, Object.class),
                        StubbingSites.array(TestStubbingSite.INSTANCE, Object.class)
                )
                .accepts(String[].class)
                .andStubs(new String[]{"Foo", "Bar", "Baz"})
                .at(
                        StubbingSites.array(TestStubbingSite.INSTANCE, String.class),
                        StubbingSites.array(TestStubbingSite.INSTANCE, String.class),
                        StubbingSites.array(TestStubbingSite.INSTANCE, String.class)
                )
                .rejects(Object.class)
                .rejects(String.class)
                .test(
                        Stubbers.array(context -> 3),
                        Stubbers.array(3)
                );
    }

}