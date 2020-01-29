package ch.leadrian.stubr.core.strategy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalStubbingModeTest {

    @Test
    void emptyShouldReturnEmptyOptionalStubber() {
        OptionalStubbingStrategy strategy = OptionalStubbingMode.EMPTY.getStrategy();

        assertThat(strategy)
                .isEqualTo(OptionalStubbingStrategy.EMPTY);
    }

    @Test
    void presentShouldReturnPresentOptionalStubber() {
        OptionalStubbingStrategy strategy = OptionalStubbingMode.PRESENT.getStrategy();

        assertThat(strategy)
                .isEqualTo(OptionalStubbingStrategy.PRESENT);
    }

    @Test
    void presentIfPossibleShouldReturnPresentIfPossibleOptionalStubber() {
        OptionalStubbingStrategy strategy = OptionalStubbingMode.PRESENT_IF_POSSIBLE.getStrategy();

        assertThat(strategy)
                .isEqualTo(OptionalStubbingStrategy.PRESENT_IF_POSSIBLE);
    }

}