package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;
import ch.leadrian.stubr.junit.StubrTest.Level0RootStubberProvider;
import ch.leadrian.stubr.junit.StubrTest.Level0StubberProvider;
import ch.leadrian.stubr.junit.StubrTest.SequenceStubberProvider;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.RootStubberBaseline;
import ch.leadrian.stubr.junit.annotation.Stub;
import ch.leadrian.stubr.junit.annotation.StubWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Locale;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.annotatedWith;
import static ch.leadrian.stubr.junit.annotation.RootStubberBaseline.Variant.MINIMAL;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(Stubr.class)
@StubWith({Level0StubberProvider.class, SequenceStubberProvider.class})
@Include(Level0RootStubberProvider.class)
class StubrTest {

    @Test
    void shouldStubValueFromStubber(@Stub String value) {
        assertThat(value)
                .isEqualTo("int-value");
    }

    @Test
    void shouldStubListValueFromStubber(@Stub List<String> value) {
        assertThat(value)
                .containsExactly("int-value");
    }

    @Test
    void shouldStubValueFromRootStubber(@Stub long value) {
        assertThat(value)
                .isEqualTo(1234L);
    }

    @Test
    void shouldStubSequenceValues(@Sequence @Stub int value1, @Sequence @Stub int value2, @Stub int value3) {
        assertAll(
                () -> assertThat(value1).isEqualTo(0),
                () -> assertThat(value2).isEqualTo(1),
                () -> assertThat(value3).isEqualTo(1337)
        );
    }

    @Nested
    @StubWith(Level1StubberProvider.class)
    @Include(Level1RootStubberProvider.class)
    class Level1Test {

        @Test
        void shouldOverrideValueFromStubber(@Stub String value) {
            assertThat(value)
                    .isEqualTo("overridden-int-value");
        }

        @Test
        void shouldOverrideValueFromRootStubber(@Stub long value) {
            assertThat(value)
                    .isEqualTo(65536L);
        }

        @Test
        void shouldNonOverriddenValueFromStubber(@Stub Foo value) {
            assertThat(value)
                    .isEqualTo(Foo.FOO);
        }

        @Test
        void shouldNonOverriddenValueFromRootStubber(@Stub Locale value) {
            assertThat(value)
                    .isEqualTo(Locale.GERMANY);
        }

    }

    @Nested
    class MinimalStubsTest {

        @Test
        @RootStubberBaseline(MINIMAL)
        void shouldStubWithMinimalValue(@Stub List<String> value) {
            assertThat(value)
                    .isEmpty();
        }

    }

    static final class SequenceStubberProvider implements StubberProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbers(ExtensionContext extensionContext) {
            return asList(
                    StubbingStrategies.constantValue(int.class, 1337),
                    StubbingStrategies.suppliedValue(int.class, sequenceNumber -> sequenceNumber).when(annotatedSiteIs(annotatedWith(Sequence.class))),
                    StubbingStrategies.enumValue()
            );
        }

    }

    static final class Level0StubberProvider implements StubberProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbers(ExtensionContext extensionContext) {
            return singletonList(StubbingStrategies.constantValue("int-value"));
        }

    }

    static final class Level0RootStubberProvider implements RootStubberProvider {

        @Override
        public List<? extends Stubber> getRootStubbers(ExtensionContext extensionContext) {
            return singletonList(Stubber.builder()
                    .stubWith(StubbingStrategies.constantValue(long.class, 1234L))
                    .stubWith(StubbingStrategies.constantValue(Locale.GERMANY))
                    .build());
        }

    }

    static final class Level1StubberProvider implements StubberProvider {

        @Override
        public List<? extends StubbingStrategy> getStubbers(ExtensionContext extensionContext) {
            return singletonList(StubbingStrategies.constantValue("overridden-int-value"));
        }

    }

    static final class Level1RootStubberProvider implements RootStubberProvider {

        @Override
        public List<? extends Stubber> getRootStubbers(ExtensionContext extensionContext) {
            return singletonList(Stubber.builder()
                    .stubWith(StubbingStrategies.constantValue(long.class, 65536L))
                    .build());
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    private @interface Sequence {
    }

    enum Foo {
        FOO
    }

}