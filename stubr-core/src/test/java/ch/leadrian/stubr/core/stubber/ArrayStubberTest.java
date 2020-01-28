package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.testing.StubberTester;
import ch.leadrian.stubr.core.testing.TestStubbingSite;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
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
                .rejects(new TypeLiteral<List<String[]>>() {})
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
                .rejects(new TypeLiteral<List<String[]>>() {})
                .test(
                        Stubbers.array(context -> 3),
                        Stubbers.array(3)
                );
    }

    @TestFactory
    Stream<DynamicTest> testPrimitiveArray() {
        return StubberTester.stubberTester()
                .provideStub(boolean.class, true, false)
                .provideStub(byte.class, (byte) 1, (byte) 2)
                .provideStub(short.class, (short) 3, (short) 4)
                .provideStub(char.class, 'a', 'b')
                .provideStub(int.class, 5, 6)
                .provideStub(long.class, 7L, 8L)
                .provideStub(float.class, 9f, 10f)
                .provideStub(double.class, 11.0, 12.0)
                .accepts(boolean[].class)
                .andStubs(new boolean[]{true, false})
                .accepts(byte[].class)
                .andStubs(new byte[]{1, 2})
                .accepts(short[].class)
                .andStubs(new short[]{3, 4})
                .accepts(char[].class)
                .andStubs(new char[]{'a', 'b'})
                .accepts(int[].class)
                .andStubs(new int[]{5, 6})
                .accepts(long[].class)
                .andStubs(new long[]{7L, 8L})
                .accepts(float[].class)
                .andStubs(new float[]{9f, 10f})
                .accepts(float[].class)
                .andStubs(new float[]{9f, 10f})
                .accepts(double[].class)
                .andStubs(new double[]{11.0, 12.0})
                .test(
                        Stubbers.array(context -> 2),
                        Stubbers.array(2)
                );
    }

}