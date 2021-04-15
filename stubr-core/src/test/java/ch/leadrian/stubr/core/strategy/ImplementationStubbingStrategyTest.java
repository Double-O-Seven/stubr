/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.TestStubbingSite;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;

class ImplementationStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testImplementationStubberWithNonParameterizedClass() {
        return stubbingStrategyTester()
                .provideStub("Test")
                .accepts(CharSequence.class)
                .andStubs("Test")
                .at(TestStubbingSite.INSTANCE)
                .rejects(String.class)
                .rejects(Object.class)
                .test(
                        StubbingStrategies.implementation(CharSequence.class, String.class),
                        StubbingStrategies.implementation(new TypeLiteral<CharSequence>() {}, new TypeLiteral<String>() {}),
                        StubbingStrategies.implementation(CharSequence.class, new TypeLiteral<String>() {}),
                        StubbingStrategies.implementation(new TypeLiteral<CharSequence>() {}, String.class)
                );
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testImplementationStubberWithParameterizedClass() {
        return stubbingStrategyTester()
                .provideStub(new TypeLiteral<List<String>>() {}, singletonList("Test"))
                .accepts(new TypeLiteral<Collection<? extends CharSequence>>() {})
                .andStubs(singletonList("Test"))
                .at(TestStubbingSite.INSTANCE)
                .rejects(new TypeLiteral<Collection<CharSequence>>() {})
                .rejects(new TypeLiteral<Collection<String>>() {})
                .rejects(new TypeLiteral<List<String>>() {})
                .rejects(String.class)
                .rejects(Object.class)
                .test(StubbingStrategies.implementation(new TypeLiteral<Collection<? extends CharSequence>>() {}, new TypeLiteral<List<String>>() {}));
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testImplementationStubberWithGenericArrayType() {
        TypeLiteral<List<String>[]> listOfStringsArray = new TypeLiteral<List<String>[]>() {};
        TypeLiteral<Collection<String>[]> collectionOfStringsArray = new TypeLiteral<Collection<String>[]>() {};
        return stubbingStrategyTester()
                .provideStub(listOfStringsArray, new List[]{singletonList("Test")})
                .accepts(collectionOfStringsArray)
                .andStubs(new List[]{singletonList("Test")})
                .at(TestStubbingSite.INSTANCE)
                .rejects(String.class)
                .rejects(Object.class)
                .test(StubbingStrategies.implementation(collectionOfStringsArray, listOfStringsArray));
    }

}