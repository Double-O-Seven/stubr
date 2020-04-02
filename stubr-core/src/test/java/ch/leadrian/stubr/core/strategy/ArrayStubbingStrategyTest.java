/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.core.testing.StubbingStrategyTester;
import ch.leadrian.stubr.core.testing.TestStubbingSite;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

class ArrayStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testEmptyArray() {
        return StubbingStrategyTester.stubbingStrategyTester()
                .accepts(Object[].class)
                .andStubs(new Object[0])
                .accepts(String[].class)
                .andStubs(new String[0])
                .rejects(Object.class)
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String[]>>() {})
                .test(
                        StubbingStrategies.array(context -> 0),
                        StubbingStrategies.array(0),
                        StubbingStrategies.array()
                );
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyArray() {
        return StubbingStrategyTester.stubbingStrategyTester()
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
                        StubbingStrategies.array(context -> 3),
                        StubbingStrategies.array(3)
                );
    }

    @TestFactory
    Stream<DynamicTest> testPrimitiveArray() {
        return StubbingStrategyTester.stubbingStrategyTester()
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
                        StubbingStrategies.array(context -> 2),
                        StubbingStrategies.array(2)
                );
    }

}