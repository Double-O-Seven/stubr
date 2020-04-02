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

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;

class DefaultValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testDefaultValueStubber() {
        return stubbingStrategyTester()
                .accepts(byte.class)
                .andStubs((byte) 0)
                .accepts(Byte.class)
                .andStubs((byte) 0)
                .accepts(short.class)
                .andStubs((short) 0)
                .accepts(Short.class)
                .andStubs((short) 0)
                .accepts(char.class)
                .andStubs('\0')
                .accepts(Character.class)
                .andStubs('\0')
                .accepts(int.class)
                .andStubs(0)
                .accepts(Integer.class)
                .andStubs(0)
                .accepts(long.class)
                .andStubs(0L)
                .accepts(Long.class)
                .andStubs(0L)
                .accepts(float.class)
                .andStubs(0f)
                .accepts(Float.class)
                .andStubs(0f)
                .accepts(double.class)
                .andStubs(0.0)
                .accepts(Double.class)
                .andStubs(0.0)
                .accepts(boolean.class)
                .andStubs(false)
                .accepts(Boolean.class)
                .andStubs(false)
                .rejects(String.class)
                .rejects(Object.class)
                .test(StubbingStrategies.defaultValue());
    }

}