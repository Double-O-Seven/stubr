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

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.matcher.MatchersTest.Wrapper.Nonnull;
import ch.leadrian.stubr.core.site.AnnotatedStubbingSite;
import ch.leadrian.stubr.core.site.ConstructorStubbingSite;
import ch.leadrian.stubr.core.site.ExecutableStubbingSite;
import ch.leadrian.stubr.core.site.MethodStubbingSite;
import ch.leadrian.stubr.core.site.ParameterStubbingSite;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;

class MatchersTest {

    @Nested
    class AnnotatedElementTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenAnnotatedStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
            StubbingSite site = mock(AnnotatedStubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.annotatedElement((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isEqualTo(delegateMatches);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNoAnnotatedStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
            StubbingSite site = mock(StubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.annotatedElement((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Test
    void anyShouldReturnTrue() {
        Matcher<Object> matcher = Matchers.any();

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isTrue();
    }

    @Nested
    class ConstructorTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenConstructorStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
            StubbingSite site = mock(ConstructorStubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.constructor((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isEqualTo(delegateMatches);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNoConstructorStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
            StubbingSite site = mock(StubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.constructor((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nested
    class ExecutableTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenExecutableStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
            StubbingSite site = mock(ExecutableStubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.executable((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isEqualTo(delegateMatches);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNoExecutableStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
            StubbingSite site = mock(StubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.executable((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nested
    class EqualToTests {

        @Test
        void givenValuesAreEqualItShouldMatch() {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<String> matcher = Matchers.equalTo("ABC123");

            boolean matches = matcher.matches(context, "ABC123");

            assertThat(matches)
                    .isTrue();
        }

        @Test
        void givenValuesAreNotEqualItShouldNotMatch() {
            StubbingContext context = mock(StubbingContext.class);
            Matcher<String> matcher = Matchers.equalTo("ABC123");

            boolean matches = matcher.matches(context, "bla");

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nested
    class MethodTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenMethodStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
            StubbingSite site = mock(MethodStubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.method((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isEqualTo(delegateMatches);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNoMethodStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
            StubbingSite site = mock(StubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.method((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isFalse();
        }

    }

    @ParameterizedTest
    @CsvSource({
            "true, false",
            "false, true"
    })
    void notShouldNegateCondition(boolean inputValue, boolean expectedValue) {
        Matcher<Object> matcher = Matchers.not(((context, value) -> inputValue));

        boolean matches = matcher.matches(mock(StubbingContext.class), "Test");

        assertThat(matches)
                .isEqualTo(expectedValue);
    }

    @Nested
    class NullableTests {

        @Test
        void givenTargetIsAnnotatedWithNullableNullableMatcherShouldMatch() {
            Matcher<AnnotatedElement> matcher = Matchers.nullable();

            boolean matches = matcher.matches(mock(StubbingContext.class), Foo.class);

            assertThat(matches)
                    .isTrue();
        }

        @TestFactory
        Stream<DynamicTest> givenTargetIsNotAnnotatedWithNullableNullableMatcherShouldNotMatch() {
            return Stream.of(Bar.class, Baz.class, Qux.class, Bla.class)
                    .map(clazz -> dynamicTest(String.format("%s should not match", clazz), () -> {
                        Matcher<AnnotatedElement> matcher = Matchers.nullable();

                        boolean matches = matcher.matches(mock(StubbingContext.class), clazz);

                        assertThat(matches)
                                .isFalse();
                    }));
        }

    }

    @Nested
    class NonNullTests {

        @TestFactory
        Stream<DynamicTest> givenTargetIsAnnotatedWithNonNullNonNullMatcherShouldMatch() {
            return Stream.of(Bar.class, Baz.class, Qux.class)
                    .map(clazz -> dynamicTest(String.format("%s should match", clazz), () -> {
                        Matcher<AnnotatedElement> matcher = Matchers.nonNull();

                        boolean matches = matcher.matches(mock(StubbingContext.class), clazz);

                        assertThat(matches)
                                .isTrue();
                    }));
        }

        @TestFactory
        Stream<DynamicTest> givenTargetIsNotAnnotatedWithNonNullNonNullMatcherShouldNotMatch() {
            return Stream.of(Foo.class, Bla.class)
                    .map(clazz -> dynamicTest(String.format("%s should not match", clazz), () -> {
                        Matcher<AnnotatedElement> matcher = Matchers.nonNull();

                        boolean matches = matcher.matches(mock(StubbingContext.class), clazz);

                        assertThat(matches)
                                .isFalse();
                    }));
        }

    }

    @Nested
    class ParameterTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenParameterStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
            StubbingSite site = mock(ParameterStubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.parameter((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isEqualTo(delegateMatches);
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void givenNoParameterStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
            StubbingSite site = mock(StubbingSite.class);
            StubbingContext context = mock(StubbingContext.class);
            Matcher<StubbingSite> matcher = Matchers.parameter((ctx, value) -> delegateMatches);

            boolean matches = matcher.matches(context, site);

            assertThat(matches)
                    .isFalse();
        }

    }

    @Nullable
    static class Foo {
    }

    @NotNull
    static class Bar {
    }

    @NonNull
    static class Baz {
    }

    @Nonnull
    static class Qux {
    }

    static class Bla {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Nullable {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NotNull {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NonNull {
    }

    @SuppressWarnings("unused")
    static class Wrapper {

        @Retention(RetentionPolicy.RUNTIME)
        @interface Nonnull {
        }

    }

}
