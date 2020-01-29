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

class RootStubberTest {

    @Nested
    class BuilderTest {

        @Test
        void shouldUseMatchingStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(testStubber(Float.class, 1f))
                    .stubWith(testStubber(Integer.class, 2))
                    .stubWith(testStubber(String.class, "Test"))
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(testStubber(Integer.class, 1))
                    .stubWith(testStubber(Integer.class, 2))
                    .stubWith(testStubber(Integer.class, 3))
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubberFromVarargs() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(
                            testStubber(Integer.class, 1),
                            testStubber(Integer.class, 2),
                            testStubber(Integer.class, 3)
                    )
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredMatchingStubberFromIterable() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(asList(
                            testStubber(Integer.class, 1),
                            testStubber(Integer.class, 2),
                            testStubber(Integer.class, 3)
                    ))
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(3);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(testStubber(Integer.class, 1), (context, type) -> context.getSite() == TestStubbingSite.FOO)
                    .stubWith(testStubber(Integer.class, 2), (context, type) -> context.getSite() == TestStubbingSite.FOO)
                    .stubWith(testStubber(Integer.class, 3), (context, type) -> context.getSite() == TestStubbingSite.BAR)
                    .build();

            Object value = rootStubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubberFromVarargs() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(
                            testStubber(Integer.class, 1).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStubber(Integer.class, 2).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStubber(Integer.class, 3).when((context, type) -> context.getSite() == TestStubbingSite.BAR)
                    )
                    .build();

            Object value = rootStubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @Test
        void shouldUseLastConfiguredConditionalMatchingStubberFromIterable() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(asList(
                            testStubber(Integer.class, 1).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStubber(Integer.class, 2).when((context, type) -> context.getSite() == TestStubbingSite.FOO),
                            testStubber(Integer.class, 3).when((context, type) -> context.getSite() == TestStubbingSite.BAR)
                    ))
                    .build();

            Object value = rootStubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(value)
                    .isEqualTo(2);
        }

        @ParameterizedTest
        @CsvSource({
                "FOO, 2",
                "BAR, 4"
        })
        void shouldUseLastConfiguredConditionalMatchingStubberFromMultipleIterables(TestStubbingSite site, int expectedValue) {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith()
                    .stubWith(
                            asList(testStubber(Integer.class, 1), testStubber(Integer.class, 2)),
                            (context, type) -> context.getSite() == TestStubbingSite.FOO
                    )
                    .stubWith(
                            asList(testStubber(Integer.class, 3), testStubber(Integer.class, 4)),
                            (context, type) -> context.getSite() == TestStubbingSite.BAR
                    )
                    .build();

            Object value = rootStubber.stub(Integer.class, site);

            assertThat(value)
                    .isEqualTo(expectedValue);
        }

        @Test
        void shouldStubValueWithIncludedRootStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .include(RootStubber.builder()
                            .stubWith(testStubber(Integer.class, 1337))
                            .build())
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1337);
        }

        @Test
        void shouldUseValueOfLastConfiguredIncludedRootStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .include(RootStubber.builder()
                            .stubWith(testStubber(Integer.class, 1337))
                            .build())
                    .include(RootStubber.builder()
                            .stubWith(testStubber(Integer.class, 1234))
                            .build())
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1234);
        }

        @Test
        void shouldOverrideStubValueOfRootStubber() {
            RootStubber rootStubber = RootStubber.builder()
                    .include(RootStubber.builder()
                            .stubWith(testStubber(Integer.class, 1337))
                            .build())
                    .stubWith(testStubber(Integer.class, 1234))
                    .build();

            Object value = rootStubber.stub(Integer.class);

            assertThat(value)
                    .isEqualTo(1234);
        }

    }

    @Nested
    class TryToStubTest<T> {

        @Test
        void givenMatchingValueForTypeAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<?> result = rootStubber.tryToStub((Type) Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenNoMatchingValueForTypeAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<?> result = rootStubber.tryToStub((Type) Float.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenMatchingValueForClassItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(Integer.class);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingPrimitiveValueForClassItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(int.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(int.class);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingValueForClassAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingPrimitiveValueForClassAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(int.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(int.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenClassMismatchForClassItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(Integer.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForClassAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(Integer.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForClassItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Float> result = rootStubber.tryToStub(Float.class);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoMatchingValueForClassAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Float> result = rootStubber.tryToStub(Float.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenMatchingValueForTypeLiteralItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(new TypeLiteral<Integer>() {});

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenMatchingValueForTypeLiteralAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Integer> result = rootStubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.success(1337));
        }

        @Test
        void givenClassMismatchForTypeLiteralItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<Integer>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForTypeLiteralAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForTypeLiteralItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Float> result = rootStubber.tryToStub(new TypeLiteral<Float>() {});

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoMatchingValueForTypeLiteralAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Result<Float> result = rootStubber.tryToStub(new TypeLiteral<Float>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(Result.failure());
        }

        @Test
        void givenNoRawTypeForTypeLiteralItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<T>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

        @Test
        void givenNoRawTypeForTypeLiteralAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<T>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

    }

    @Nested
    class StubTest<T> {

        @Test
        void givenMatchingValueForTypeAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Object result = rootStubber.stub((Type) Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenNoMatchingValueForTypeAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.stub((Type) Float.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessage("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenMatchingValueForClassItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Integer result = rootStubber.stub(Integer.class);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingPrimitiveValueForClassItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(int.class, 1337))
                    .build();

            int result = rootStubber.stub(int.class);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingValueForClassAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Integer result = rootStubber.stub(Integer.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingPrimitiveValueForClassAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(int.class, 1337))
                    .build();

            int result = rootStubber.stub(int.class, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenClassMismatchForClassItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(Integer.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForClassAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(Integer.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForClassItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.stub(Float.class));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessage("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoMatchingValueForClassAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.stub(Float.class, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessage("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenMatchingValueForTypeLiteralItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Integer result = rootStubber.stub(new TypeLiteral<Integer>() {});

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenMatchingValueForTypeLiteralAndSiteItShouldReturnSuccess() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Integer result = rootStubber.stub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO);

            assertThat(result)
                    .isEqualTo(1337);
        }

        @Test
        void givenClassMismatchForTypeLiteralItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<Integer>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenClassMismatchForTypeLiteralAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, "Test"))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<Integer>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        void givenNoMatchingValueForTypeLiteralItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.stub(new TypeLiteral<Float>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessage("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoMatchingValueForTypeLiteralAndSiteItShouldReturnFailure() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.stub(new TypeLiteral<Float>() {}, TestStubbingSite.FOO));

            assertThat(caughtThrowable)
                    .isInstanceOf(StubbingException.class)
                    .hasMessage("Failed to stub instance of class java.lang.Float");
        }

        @Test
        void givenNoRawTypeForTypeLiteralItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<T>() {}));

            assertThat(caughtThrowable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot get raw type of T");
        }

        @Test
        void givenNoRawTypeForTypeLiteralAndSiteItShouldThrowException() {
            RootStubber rootStubber = RootStubber.builder()
                    .stubWith(testStubber(Integer.class, 1337))
                    .build();

            Throwable caughtThrowable = catchThrowable(() -> rootStubber.tryToStub(new TypeLiteral<T>() {}, TestStubbingSite.FOO));

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

    static Stubber testStubber(Type type, Object value) {
        return new TestStubber(type, value);
    }

    private static class TestStubber implements Stubber {

        private final Type type;
        private final Object value;

        private TestStubber(Type type, Object value) {
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