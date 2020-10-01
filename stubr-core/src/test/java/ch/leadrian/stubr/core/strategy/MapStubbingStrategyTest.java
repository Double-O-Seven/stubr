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
import ch.leadrian.stubr.core.TestStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;

class MapStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testEmptyMapStubber() {
        return stubbingStrategyTester()
                .accepts(Map.class)
                .andStubs(new HashMap<>())
                .accepts(new TypeLiteral<Map<String, Integer>>() {})
                .andStubs(new HashMap<>())
                .accepts(new TypeLiteral<Map<? super String, ? super Integer>>() {})
                .andStubs(new HashMap<>())
                .accepts(new TypeLiteral<Map<?, ?>>() {})
                .andStubs(new HashMap<>())
                .accepts(new TypeLiteral<Map<? extends String, ? extends Integer>>() {})
                .andStubs(new HashMap<>())
                .rejects(HashMap.class)
                .rejects(new TypeLiteral<HashMap<String, String>>() {})
                .rejects(new TypeLiteral<HashMap<? super String, ? super Integer>>() {})
                .rejects(new TypeLiteral<HashMap<? extends String, ? extends Integer>>() {})
                .rejects(new TypeLiteral<Map<String, Integer>[]>() {})
                .test(
                        StubbingStrategies.map(Map.class, HashMap::new, context -> 0),
                        StubbingStrategies.map(Map.class, HashMap::new, 0),
                        StubbingStrategies.map(Map.class, HashMap::new)
                );
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyMapStubber() {
        ParameterizedTypeLiteral<Map<String, Integer>> mapOfStrings = new ParameterizedTypeLiteral<Map<String, Integer>>() {};
        return stubbingStrategyTester()
                .provideStub(String.class, "foo", "bar", "baz")
                .provideStub(Integer.class, 123, 456, 789)
                .rejects(Map.class)
                .accepts(mapOfStrings)
                .andStubs(new HashMap<>(ImmutableMap.of("foo", 123, "bar", 456, "baz", 789)))
                .at(
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 1),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 1),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, mapOfStrings.getType(), 1)
                )
                .rejects(HashMap.class)
                .rejects(new TypeLiteral<HashMap<String, Integer>>() {})
                .rejects(new TypeLiteral<HashMap<? super String, ? super Integer>>() {})
                .rejects(new TypeLiteral<HashMap<? extends String, ? extends Integer>>() {})
                .rejects(new TypeLiteral<Map<String, Integer>[]>() {})
                .test(StubbingStrategies.map(Map.class, HashMap::new, context -> 3), StubbingStrategies.map(Map.class, HashMap::new, 3));
    }

    @TestFactory
    Stream<DynamicTest> testUnsupportedParameterization() {
        return stubbingStrategyTester()
                .rejects(new TypeLiteral<WeirdMap<String, Integer, BigDecimal>>() {})
                .test(StubbingStrategies.map(WeirdMap.class, values -> new WeirdMap(), context -> 3));
    }

    @SuppressWarnings("unused")
    private static final class WeirdMap<T, U, V> extends HashMap<T, U> {
    }

}