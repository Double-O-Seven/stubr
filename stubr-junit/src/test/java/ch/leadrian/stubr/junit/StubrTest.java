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

package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;
import ch.leadrian.stubr.junit.StubrTest.Level0StubberProvider;
import ch.leadrian.stubr.junit.StubrTest.Level0StubbingStrategyProvider;
import ch.leadrian.stubr.junit.StubrTest.SequenceStubbingStrategyProvider;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.Stub;
import ch.leadrian.stubr.junit.annotation.StubWith;
import ch.leadrian.stubr.junit.annotation.StubberBaseline;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedElement;
import static ch.leadrian.stubr.core.matcher.Matchers.annotatedWith;
import static ch.leadrian.stubr.core.matcher.Matchers.site;
import static ch.leadrian.stubr.junit.annotation.StubberBaseline.Variant.MINIMAL;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(Stubr.class)
@StubWith({Level0StubbingStrategyProvider.class, SequenceStubbingStrategyProvider.class})
@Include(Level0StubberProvider.class)
class StubrTest {

    @Test
    void shouldStubValueFromStubbingStrategy(@Stub ValueProvidedByStubbingStrategy value) {
        assertThat(value)
                .isEqualTo(ValueProvidedByStubbingStrategy.VALUE);
    }

    @Test
    void shouldStubValueFromStubber(@Stub ValueProvidedByStubber value) {
        assertThat(value)
                .isEqualTo(ValueProvidedByStubber.VALUE);
    }

    @Test
    void shouldStubValueFromStubbingStrategy(@Stub NonOverriddenValueProvidedByStubber value) {
        assertThat(value)
                .isEqualTo(NonOverriddenValueProvidedByStubber.INSTANCE);
    }

    @Test
    void shouldStubValueFromStubber(@Stub NonOverriddenValueProvidedByStubber value) {
        assertThat(value)
                .isEqualTo(NonOverriddenValueProvidedByStubber.INSTANCE);
    }

    @Test
    void shouldStubListValueStubbingStrategy(@Stub List<ValueProvidedByStubbingStrategy> value) {
        assertThat(value)
                .containsExactly(ValueProvidedByStubbingStrategy.VALUE);
    }

    @Test
    void shouldStubListValueStubber(@Stub List<ValueProvidedByStubber> value) {
        assertThat(value)
                .containsExactly(ValueProvidedByStubber.VALUE);
    }

    @Test
    void shouldStubSequenceValues(@Sequence int value1, @Sequence int value2, @Stub int value3) {
        assertAll(
                () -> assertThat(value1).isEqualTo(0),
                () -> assertThat(value2).isEqualTo(1),
                () -> assertThat(value3).isEqualTo(1337)
        );
    }

    @Nested
    @StubWith(Level1StubbingStrategyProvider.class)
    @Include(Level1StubberProvider.class)
    class Level1Test {

        @Test
        void shouldOverrideValueFromStubbingStrategy(@Stub ValueProvidedByStubbingStrategy value) {
            assertThat(value)
                    .isEqualTo(ValueProvidedByStubbingStrategy.OVERRIDE_VALUE);
        }

        @Test
        void shouldOverrideValueFromStubber(@Stub ValueProvidedByStubber value) {
            assertThat(value)
                    .isEqualTo(ValueProvidedByStubber.OVERRIDE_VALUE);
        }

        @Test
        void shouldNotOverrideValueFromStubbingStrategy(@Stub NonOverriddenValueProvidedByStubber value) {
            assertThat(value)
                    .isEqualTo(NonOverriddenValueProvidedByStubber.INSTANCE);
        }

        @Test
        void shouldNotOverrideValueFromStubber(@Stub NonOverriddenValueProvidedByStubber value) {
            assertThat(value)
                    .isEqualTo(NonOverriddenValueProvidedByStubber.INSTANCE);
        }

        @Test
        void shouldOverrideListValueStubbingStrategy(@Stub List<ValueProvidedByStubbingStrategy> value) {
            assertThat(value)
                    .containsExactly(ValueProvidedByStubbingStrategy.OVERRIDE_VALUE);
        }

        @Test
        void shouldOverrideListValueStubber(@Stub List<ValueProvidedByStubber> value) {
            assertThat(value)
                    .containsExactly(ValueProvidedByStubber.OVERRIDE_VALUE);
        }

        @Test
        void shouldStubSequenceValues(@Sequence int value1, @Sequence int value2, @Stub int value3) {
            assertAll(
                    () -> assertThat(value1).isEqualTo(0),
                    () -> assertThat(value2).isEqualTo(1),
                    () -> assertThat(value3).isEqualTo(1337)
            );
        }

    }

    @Nested
    class MinimalStubsTest {

        @Test
        @StubberBaseline(MINIMAL)
        void shouldStubWithMinimalValue(@Stub List<ValueProvidedByStubbingStrategy> value) {
            assertThat(value)
                    .isEmpty();
        }

    }

    static final class SequenceStubbingStrategyProvider implements StubbingStrategyProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext extensionContext) {
            return asList(
                    StubbingStrategies.constantValue(int.class, 1337),
                    StubbingStrategies.suppliedValue(int.class, sequenceNumber -> sequenceNumber).when(site(annotatedElement(annotatedWith(Sequence.class))))
            );
        }

    }

    static final class Level0StubbingStrategyProvider implements StubbingStrategyProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext extensionContext) {
            return asList(
                    StubbingStrategies.constantValue(ValueProvidedByStubbingStrategy.VALUE),
                    StubbingStrategies.constantValue(NonOverriddenValueProvidedByStubbingStrategy.INSTANCE)
            );
        }

    }

    static final class Level0StubberProvider implements StubberProvider {

        @Override
        public List<? extends Stubber> getStubbers(ExtensionContext extensionContext) {
            return singletonList(Stubber.builder()
                    .stubWith(StubbingStrategies.constantValue(ValueProvidedByStubber.VALUE))
                    .stubWith(StubbingStrategies.constantValue(NonOverriddenValueProvidedByStubber.INSTANCE))
                    .build());
        }

    }

    static final class Level1StubbingStrategyProvider implements StubbingStrategyProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext extensionContext) {
            return singletonList(StubbingStrategies.constantValue(ValueProvidedByStubbingStrategy.OVERRIDE_VALUE));
        }

    }

    static final class Level1StubberProvider implements StubberProvider {

        @Override
        public List<? extends Stubber> getStubbers(ExtensionContext extensionContext) {
            return singletonList(Stubber.builder()
                    .stubWith(StubbingStrategies.constantValue(ValueProvidedByStubber.OVERRIDE_VALUE))
                    .build());
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @Stub
    private @interface Sequence {
    }

    private static class ValueProvidedByStubbingStrategy {

        private static final ValueProvidedByStubbingStrategy VALUE = new ValueProvidedByStubbingStrategy();
        private static final ValueProvidedByStubbingStrategy OVERRIDE_VALUE = new ValueProvidedByStubbingStrategy();

    }

    private static class ValueProvidedByStubber {

        private static final ValueProvidedByStubber VALUE = new ValueProvidedByStubber();
        private static final ValueProvidedByStubber OVERRIDE_VALUE = new ValueProvidedByStubber();

    }

    private static class NonOverriddenValueProvidedByStubbingStrategy {

        private static NonOverriddenValueProvidedByStubbingStrategy INSTANCE = new NonOverriddenValueProvidedByStubbingStrategy();

    }

    private static class NonOverriddenValueProvidedByStubber {

        private static NonOverriddenValueProvidedByStubber INSTANCE = new NonOverriddenValueProvidedByStubber();

    }

}