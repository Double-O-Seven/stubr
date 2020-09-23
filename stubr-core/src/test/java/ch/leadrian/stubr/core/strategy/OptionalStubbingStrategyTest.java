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
import java.util.Optional;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;

class OptionalStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testEmptyOptionalStubber() {
        return stubbingStrategyTester()
                .accepts(Optional.class)
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<? super String>>() {})
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<? extends String>>() {})
                .andStubs(Optional.empty())
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(StubbingStrategies.optional(OptionalStubbingMode.EMPTY));
    }

    @TestFactory
    Stream<DynamicTest> testPresentOptionalStubber() {
        return stubbingStrategyTester()
                .provideStub("Test")
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.of("Test"))
                .rejects(Optional.class)
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(StubbingStrategies.optional(OptionalStubbingMode.PRESENT));
    }

    @TestFactory
    Stream<DynamicTest> testPresentIfPossibleOptionalStubber() {
        return stubbingStrategyTester()
                .provideStub("Test")
                .doNotStub(Integer.class)
                .accepts(Optional.class)
                .andStubs(Optional.empty())
                .accepts(new TypeLiteral<Optional<String>>() {})
                .andStubs(Optional.of("Test"))
                .accepts(new TypeLiteral<Optional<Integer>>() {})
                .andStubs(Optional.empty())
                .rejects(String.class)
                .rejects(new TypeLiteral<List<String>>() {})
                .test(StubbingStrategies.optional(OptionalStubbingMode.PRESENT_IF_POSSIBLE));
    }

}