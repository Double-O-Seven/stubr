/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;

class NullValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testNullValueStubber() {
        return stubbingStrategyTester()
                .accepts(Object.class)
                .andStubs(null)
                .accepts(String.class)
                .andStubs(null)
                .accepts(new TypeLiteral<List<String>>() {})
                .andStubs(null)
                .rejects(boolean.class)
                .rejects(byte.class)
                .rejects(short.class)
                .rejects(char.class)
                .rejects(int.class)
                .rejects(long.class)
                .rejects(float.class)
                .rejects(double.class)
                .test(StubbingStrategies.nullValue());
    }

}