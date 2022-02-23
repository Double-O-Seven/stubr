/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SuppliedValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValue() {
        return stubbingStrategyTester()
                .accepts(String.class)
                .andStubs("Test")
                .rejects(Object.class)
                .test(
                        StubbingStrategies.suppliedValue(String.class, () -> "Test"),
                        StubbingStrategies.suppliedValue(String.class, sequenceNumber -> "Test"),
                        StubbingStrategies.suppliedValue(String.class, (context, sequenceNumber) -> "Test")
                );
    }

    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValueOfParameterizedType() {
        TypeLiteral<List<String>> listOfStrings = new TypeLiteral<List<String>>() {
        };
        return stubbingStrategyTester()
                .accepts(listOfStrings)
                .andStubs(singletonList("Test"))
                .rejects(new TypeLiteral<List<Integer>>() {
                })
                .test(
                        StubbingStrategies.suppliedValue(listOfStrings, () -> singletonList("Test")),
                        StubbingStrategies.suppliedValue(listOfStrings, sequenceNumber -> singletonList("Test")),
                        StubbingStrategies.suppliedValue(listOfStrings, (context, sequenceNumber) -> singletonList("Test"))
                );
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testFixedSuppliedValueOfGenericArrayTypeType() {
        TypeLiteral<List<String>[]> listOfStringsArray = new TypeLiteral<List<String>[]>() {
        };
        return stubbingStrategyTester()
                .accepts(listOfStringsArray)
                .andStubs(new List[]{singletonList("Test")})
                .rejects(new TypeLiteral<List<Integer>>() {
                })
                .test(
                        StubbingStrategies.suppliedValue(listOfStringsArray, () -> new List[]{singletonList("Test")}),
                        StubbingStrategies.suppliedValue(listOfStringsArray, sequenceNumber -> new List[]{singletonList("Test")}),
                        StubbingStrategies.suppliedValue(listOfStringsArray, (context, sequenceNumber) -> new List[]{singletonList("Test")})
                );
    }

    @Test
    void shouldSuppliedSequencedValue() {
        StubbingStrategy stubbingStrategy = StubbingStrategies.suppliedValue(Integer.class, sequenceNumber -> sequenceNumber);

        List<Object> values = IntStream.generate(() -> 0)
                .limit(3)
                .mapToObj(i -> stubbingStrategy.stub(mock(StubbingContext.class), Integer.class))
                .collect(toList());

        assertThat(values)
                .containsExactly(0, 1, 2);
    }

}