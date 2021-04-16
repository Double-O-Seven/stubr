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

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class StubberTest {

    @Nested
    class BuilderTest {

        @Test
        void shouldUseMatchingStubber() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(testStrategy(Float.class, 1f))
                    .stubWith(testStrategy(Integer.class, 2))
                    .stubWith(testStrategy(String.class, "Test"))
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubber() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(testStrategy(Integer.class, 1))
                    .stubWith(testStrategy(Integer.class, 2))
                    .stubWith(testStrategy(Integer.class, 3))
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubberFromVarargs() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(
                            testStrategy(Integer.class, 1),
                            testStrategy(Integer.class, 2),
                            testStrategy(Integer.class, 3)
                    )
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubberFromIterable() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(asList(
                            testStrategy(Integer.class, 1),
                            testStrategy(Integer.class, 2),
                            testStrategy(Integer.class, 3)
                    ))
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubber() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(testStrategy(Integer.class, 1), (context, type) -> context.getSite() == TestStubbingSite.FOO)
                    .stubWith(testStrategy(Integer.class, 2), (context, type) -> context.getSite() == TestStubbingSite.FOO)
                    .stubWith(testStrategy(Integer.class, 3), (context, type) -> context.getSite() == TestStubbingSite.BAR)
                    .build();

            Object value = stubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubberFromVarargs() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(
                            testStrategy(Integer.class, 1).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStrategy(Integer.class, 2).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStrategy(Integer.class, 3).when((context, type) -> context.getSite() == TestStubbingSite.BAR)
                    )
                    .build();

            Object value = stubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubberFromIterable() {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(asList(
                            testStrategy(Integer.class, 1).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStrategy(Integer.class, 2).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStrategy(Integer.class, 3).when((context, type) -> context.getSite() == TestStubbingSite.BAR)
                    ))
                    .build();

            Object value = stubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @ParameterizedTest
        @CsvSource({
                "FOO, 2",
                "BAR, 4"
        })
        void shouldUseLastConfiguredConditionalMatchingStubberFromMultipleIterables(TestStubbingSite site, int expectedValue) {
            Stubber stubber = Stubber.builder()
                    .stubWith()
                    .stubWith(
                            asList(testStrategy(Integer.class, 1), testStrategy(Integer.class, 2)),
                            (context, type) -> context.getSite() == TestStubbingSite.FOO
                    )
                    .stubWith(
                            asList(testStrategy(Integer.class, 3), testStrategy(Integer.class, 4)),
                            (context, type) -> context.getSite() == TestStubbingSite.BAR
                    )
                    .build();

            Object value = stubber.stub(Integer.class, site);

            assertThat(value)
                    .isEqualTo(expectedValue);
        }

        @Test
        void shouldStubValueWithIncludedStubber() {
            Stubber stubber = Stubber.builder()
                    .include(Stubber.builder()
                            .stubWith(testStrategy(Integer.class, 1337))
                            .build())
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1337);
        }

        @Test
        void shouldUseValueOfLastConfiguredIncludedStubber() {
            Stubber stubber = Stubber.builder()
                    .include(Stubber.builder()
                            .stubWith(testStrategy(Integer.class, 1337))
                            .build())
                    .include(Stubber.builder()
                            .stubWith(testStrategy(Integer.class, 1234))
                            .build())
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1234);
        }

        @Test
        void shouldOverrideStubValueOfStubber() {
            Stubber stubber = Stubber.builder()
                    .include(Stubber.builder()
                            .stubWith(testStrategy(Integer.class, 1337))
                            .build())
                    .stubWith(testStrategy(Integer.class, 1234))
                    .build();

            Object value = stubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1234);
        }

    }

    @Nested
    class TryToStubTest<T> {

        @Test
        void givenMatchingValueForTypeAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<?> result = stubber.tryToStub((Type) Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenNoMatchingValueForTypeAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<?> result = stubber.tryToStub((Type) Float.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenMatchingValueForClassItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(Integer.class);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingPrimitiveValueForClassItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(int.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(int.class);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingValueForClassAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingPrimitiveValueForClassAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(int.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(int.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenClassMismatchForClassItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(Integer.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForClassAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(Integer.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForClassItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Float> result = stubber.tryToStub(Float.class);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoMatchingValueForClassAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Float> result = stubber.tryToStub(Float.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenMatchingValueForTypeLiteralItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(new TypeLiteral<Integer>() {});

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingValueForTypeLiteralAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Integer> result = stubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenClassMismatchForTypeLiteralItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<Integer>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForTypeLiteralAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForTypeLiteralItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Float> result = stubber.tryToStub(new TypeLiteral<Float>() {});

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoMatchingValueForTypeLiteralAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Result<Float> result = stubber.tryToStub(new TypeLiteral<Float>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoRawTypeForTypeLiteralItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<T>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

        @Test
        void givenNoRawTypeForTypeLiteralAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<T>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

    }

    @Nested
    class StubTest<T> {

        @Test
        void givenMatchingValueForTypeAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Object result = stubber.stub((Type) Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenNoMatchingValueForTypeAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.stub((Type) Float.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessageStartingWith("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenMatchingValueForClassItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Integer result = stubber.stub(Integer.class);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingPrimitiveValueForClassItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(int.class, 1337))
                    .build();

            int result = stubber.stub(int.class);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingValueForClassAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Integer result = stubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingPrimitiveValueForClassAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(int.class, 1337))
                    .build();

            int result = stubber.stub(int.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenClassMismatchForClassItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(Integer.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForClassAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(Integer.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForClassItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.stub(Float.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessageStartingWith("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoMatchingValueForClassAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.stub(Float.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessageStartingWith("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenMatchingValueForTypeLiteralItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Integer result = stubber.stub(new TypeLiteral<Integer>() {});

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingValueForTypeLiteralAndSiteItShouldReturnSuccess() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Integer result = stubber.stub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenClassMismatchForTypeLiteralItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<Integer>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForTypeLiteralAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForTypeLiteralItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.stub(new TypeLiteral<Float>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessageStartingWith("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoMatchingValueForTypeLiteralAndSiteItShouldReturnFailure() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.stub(new TypeLiteral<Float>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessageStartingWith("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoRawTypeForTypeLiteralItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<T>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

        @Test
        void givenNoRawTypeForTypeLiteralAndSiteItShouldThrowException() {
            Stubber stubber = Stubber.builder()
                    .stubWith(testStrategy(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> stubber.tryToStub(new TypeLiteral<T>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

    }

    private enum TestStubbingSite implements StubbingSite {
        FOO,
        BAR;

        @Override
        public Optional<? extends StubbingSite> getParent() {
            return Optional.empty();
        }
    }

    static StubbingStrategy testStrategy(Type type, Object value) {
        return new TestStubbingStrategy(type, value);
    }

    private static class TestStubbingStrategy implements StubbingStrategy {

        private final Type type;
        private final Object value;

        private TestStubbingStrategy(Type type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean accepts(StubbingContext context, Type type) {
            return this.type.equals(type);
        }

        @Override
        public Object stub(StubbingContext context, Type type) {
            return value;
        }

    }

}