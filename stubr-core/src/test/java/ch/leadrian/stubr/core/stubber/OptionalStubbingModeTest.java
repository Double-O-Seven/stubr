package ch.leadrian.stubr.core.stubber;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalStubbingModeTest {

    @Test
    void emptyShouldReturnEmptyOptionalStubber() {
        OptionalStubber stubber = OptionalStubbingMode.EMPTY.getStubber();

        assertThat(stubber)
                .isEqualTo(OptionalStubber.EMPTY);
    }

    @Test
    void presentShouldReturnPresentOptionalStubber() {
        OptionalStubber stubber = OptionalStubbingMode.PRESENT.getStubber();

        assertThat(stubber)
                .isEqualTo(OptionalStubber.PRESENT);
    }

    @Test
    void presentIfPossibleShouldReturnPresentIfPossibleOptionalStubber() {
        OptionalStubber stubber = OptionalStubbingMode.PRESENT_IF_POSSIBLE.getStubber();

        assertThat(stubber)
                .isEqualTo(OptionalStubber.PRESENT_IF_POSSIBLE);
    }

}