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

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.type.TypeLiteral;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class ProxyStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testNonCachingProxyStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 2, 3),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(StubbingStrategies.proxy(false));
    }

    @TestFactory
    Stream<DynamicTest> testCachingProxyStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 1, 1),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(
                        StubbingStrategies.proxy(true),
                        StubbingStrategies.proxy()
                );
    }

    @TestFactory
    Stream<DynamicTest> testGenericProxies() {
        return stubbingStrategyTester()
                .provideStub("Test")
                .provideStub(1234)
                .accepts(new TypeLiteral<Fubar<String>>() {})
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Fubar.class, fubar ->
                        assertThat(fubar.getGenericValue()).isEqualTo("Test")))
                .accepts(new TypeLiteral<Fubar<Integer>>() {})
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Fubar.class, fubar ->
                        assertThat(fubar.getGenericValue()).isEqualTo(1234)))
                .test(
                        StubbingStrategies.proxy(true),
                        StubbingStrategies.proxy()
                );
    }

    @Test
    void testProxyEquals() {
        StubbingContext context = mock(StubbingContext.class);
        StubbingStrategy stubbingStrategy = StubbingStrategies.proxy();

        Foo foo1 = (Foo) stubbingStrategy.stub(context, Foo.class);
        Foo foo2 = (Foo) stubbingStrategy.stub(context, Foo.class);

        new EqualsTester()
                .addEqualityGroup(foo1, foo1)
                .addEqualityGroup(foo2, foo2)
                .addEqualityGroup("Test")
                .testEquals();
    }

    @Test
    void toStringShouldNotReturnStubbedString() {
        StubbingContext context = mock(StubbingContext.class);
        StubbingStrategy stubbingStrategy = StubbingStrategies.proxy();

        Foo foo = (Foo) stubbingStrategy.stub(context, Foo.class);

        assertThat(foo.toString())
                .startsWith("Stubbed ch.leadrian.stubr.core.strategy.ProxyStubbingStrategyTest$Foo");
    }

    private interface Foo {

        int getInt();

        default int getAnotherInt() {
            return 1234;
        }

        String getString();

    }

    private abstract class Bar {
    }

    private class Qux {
    }

    private interface Fubar<T> {

        T getGenericValue();

    }

}