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

import ch.leadrian.stubr.core.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;

class ConstantValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testConstantValueStubber() {
        return stubbingStrategyTester()
                .accepts(BigDecimal.class)
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? extends BigDecimal>>() {}.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .accepts(new ParameterizedTypeLiteral<List<? super BigDecimal>>() {}.getActualTypeArgument(0))
                .andStubs(new BigDecimal(1337))
                .rejects(Number.class)
                .rejects(new BigDecimal(1337) {}.getClass())
                .test(
                        StubbingStrategies.constantValue(new BigDecimal(1337)),
                        StubbingStrategies.constantValue(BigDecimal.class, new BigDecimal(1337)),
                        StubbingStrategies.constantValue(new TypeLiteral<BigDecimal>() {}, new BigDecimal(1337))
                );
    }

    @TestFactory
    Stream<DynamicTest> testConstantValueStubberWithParameterizedType() {
        TypeLiteral<List<String>> listOfStrings = new TypeLiteral<List<String>>() {};
        return stubbingStrategyTester()
                .accepts(listOfStrings)
                .andStubs(singletonList("Test"))
                .rejects(new TypeLiteral<List<Integer>>() {})
                .test(StubbingStrategies.constantValue(listOfStrings, singletonList("Test")));
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testConstantValueStubberWithGenericArrayType() {
        TypeLiteral<List<String>[]> listOfStringsArray = new TypeLiteral<List<String>[]>() {};
        return stubbingStrategyTester()
                .accepts(listOfStringsArray)
                .andStubs(new List[]{singletonList("Test")})
                .rejects(new TypeLiteral<List<Integer>>() {})
                .test(StubbingStrategies.constantValue(listOfStringsArray, new List[]{singletonList("Test")}));
    }

}