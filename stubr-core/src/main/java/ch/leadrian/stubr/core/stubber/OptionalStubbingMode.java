package ch.leadrian.stubr.core.stubber;

public enum OptionalStubbingMode {
    EMPTY(OptionalStubber.EMPTY),
    PRESENT(OptionalStubber.PRESENT),
    PRESENT_IF_POSSIBLE(OptionalStubber.PRESENT_IF_POSSIBLE);

    private final OptionalStubber stubber;

    OptionalStubbingMode(OptionalStubber stubber) {
        this.stubber = stubber;
    }

    OptionalStubber getStubber() {
        return stubber;
    }
}
