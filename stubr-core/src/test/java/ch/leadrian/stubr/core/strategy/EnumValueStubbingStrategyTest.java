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

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static com.google.common.collect.MoreCollectors.toOptional;

class
EnumValueStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testDefaultEnumValueStubber() {
        return stubbingStrategyTester()
                .accepts(Foo.class)
                .andStubs(Foo.FOO)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .rejects(new TypeLiteral<List<Foo>>() {
                })
                .rejects(new TypeLiteral<List<Foo>[]>() {
                })
                .test(StubbingStrategies.enumValue());
    }

    @TestFactory
    Stream<DynamicTest> testSelectiveEnumValueStubber() {
        return stubbingStrategyTester()
                .accepts(Foo.class)
                .andStubs(Foo.FU)
                .accepts(Fubar.class)
                .andStubs(Fubar.FU)
                .rejects(Bla.class)
                .rejects(Bar.class)
                .rejects(Qux.class)
                .rejects(new TypeLiteral<List<Foo>>() {
                })
                .rejects(new TypeLiteral<List<Foo>[]>() {
                })
                .test(StubbingStrategies.enumValue(((context, values) -> values.stream()
                        .filter(value -> "FU".equals(value.name()))
                        .collect(toOptional()))));
    }

    private enum Foo {
        FOO,
        FU
    }

    private enum Fubar {
        FU,
        BAR
    }

    private enum Bar {}

    private enum Bla {
        BLA,
        BLUB
    }

    private static class Qux {
    }

}