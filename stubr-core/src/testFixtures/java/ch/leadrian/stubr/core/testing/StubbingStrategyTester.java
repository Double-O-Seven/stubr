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

package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface StubbingStrategyTester {

    static StubbingStrategyTester stubbingStrategyTester() {
        return new StubbingStrategyTesterImpl();
    }

    StubbingStrategyTester provideStub(Type type, Object... values);

    default StubbingStrategyTester provideStub(Object value) {
        return provideStub(value.getClass(), value);
    }

    @SuppressWarnings("unchecked")
    default <T> StubbingStrategyTester provideStub(Class<T> type, T... values) {
        return provideStub(type, (Object[]) values);
    }

    @SuppressWarnings("unchecked")
    default <T> StubbingStrategyTester provideStub(TypeLiteral<T> typeLiteral, T... values) {
        return provideStub(typeLiteral.getType(), (Object[]) values);
    }

    StubbingStrategyTester doNotStub(Type type);

    default StubbingStrategyTester doNotStub(TypeLiteral<?> typeLiteral) {
        return doNotStub(typeLiteral.getType());
    }

    <T> StubValueTester<T> accepts(Type type);

    default <T> StubValueTester<T> accepts(Class<T> type) {
        return accepts((Type) type);
    }

    default <T> StubValueTester<T> accepts(TypeLiteral<T> typeLiteral) {
        return accepts(typeLiteral.getType());
    }

    StubbingStrategyTester rejects(Type type);

    default StubbingStrategyTester rejects(TypeLiteral<?> typeLiteral) {
        return rejects(typeLiteral.getType());
    }

    Stream<DynamicTest> test(StubbingStrategy stubbingStrategy);

    default Stream<DynamicTest> test(StubbingStrategy... stubbingStrategies) {
        return Stream.of(stubbingStrategies).flatMap(this::test);
    }

    interface StubValueTester<T> extends StubbingStrategyTester {

        SiteTester andStubs(T expectedValue);

        SiteTester andStubSatisfies(Consumer<Object> assertion);

    }

    interface SiteTester extends StubbingStrategyTester {

        StubbingStrategyTester at(StubbingSite... expectedSites);

    }

}
