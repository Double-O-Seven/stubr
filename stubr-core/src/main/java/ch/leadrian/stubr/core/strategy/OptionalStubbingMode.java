package ch.leadrian.stubr.core.strategy;

public enum OptionalStubbingMode {
    EMPTY(OptionalStubbingStrategy.EMPTY),
    PRESENT(OptionalStubbingStrategy.PRESENT),
    PRESENT_IF_POSSIBLE(OptionalStubbingStrategy.PRESENT_IF_POSSIBLE);

    private final OptionalStubbingStrategy strategy;

    OptionalStubbingMode(OptionalStubbingStrategy strategy) {
        this.strategy = strategy;
    }

    OptionalStubbingStrategy getStrategy() {
        return strategy;
    }
}
