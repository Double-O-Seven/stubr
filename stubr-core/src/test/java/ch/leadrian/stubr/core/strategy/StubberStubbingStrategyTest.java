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

import ch.leadrian.stubr.core.Stubber;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.stubber;
import static org.assertj.core.api.Assertions.assertThat;

class StubberStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testStubberStubber() {
        return stubbingStrategyTester()
                .accepts(Stubber.class)
                .andStubSatisfies(value -> assertThat(value).isInstanceOf(Stubber.class))
                .rejects(Object.class)
                .rejects(String.class)
                .test(stubber());
    }

}