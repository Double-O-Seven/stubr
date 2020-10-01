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

package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

class MockStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testMockitoStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo ->
                        assertAll(
                                () -> assertThat(foo.getInt()).isEqualTo(1337),
                                () -> assertThat(foo.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(new TypeLiteral<Bar<String>>() {})
                .rejects(int.class)
                .rejects(String.class)
                .rejects(new TypeLiteral<Bar<String>[]>() {})
                .test(MockitoStubbingStrategies.mock(Foo.class));
    }

    @TestFactory
    Stream<DynamicTest> testMockitoStubberWithParameterizedType() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1337)
                .provideStub("Test")
                .accepts(new TypeLiteral<Bar<String>>() {
                })
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Bar.class, bar ->
                        assertAll(
                                () -> assertThat(bar.getInt()).isEqualTo(1337),
                                () -> assertThat(bar.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(Foo.class)
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbingStrategies.mock(Bar.class));
    }

    @TestFactory
    Stream<DynamicTest> testMockitoStubberWithMockConfiguration() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1234)
                .provideStub("bla")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo ->
                        assertAll(
                                () -> assertThat(foo.getInt()).isEqualTo(1337),
                                () -> assertThat(foo.getString()).isEqualTo("Test")
                        )
                ))
                .rejects(int.class)
                .rejects(String.class)
                .test(MockitoStubbingStrategies.mock(Foo.class, mock -> {
                    when(mock.getInt()).thenReturn(1337);
                    when(mock.getString()).thenReturn("Test");
                }));
    }

    class Foo {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

    class Bar<T> {

        int getInt() {
            throw new UnsupportedOperationException();
        }

        String getString() {
            throw new UnsupportedOperationException();
        }

    }

}