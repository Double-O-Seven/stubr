package ch.leadrian.stubr.core.strategy;

/**
 * An enum defining the different stubbing modes for {@link java.util.Optional}s.
 */
public enum OptionalStubbingMode {
    /**
     * All {@link java.util.Optional}s are stubbed using {@link java.util.Optional#empty()}.
     */
    EMPTY(OptionalStubbingStrategy.EMPTY),
    /**
     * All {@link java.util.Optional}s require a suitable stub value.
     */
    PRESENT(OptionalStubbingStrategy.PRESENT),
    /**
     * {@link java.util.Optional}s for which a suitable stub value can be provided, will be stubbed using {@link
     * java.util.Optional#of}, else {@link java.util.Optional#empty()} will be used.
     */
    PRESENT_IF_POSSIBLE(OptionalStubbingStrategy.PRESENT_IF_POSSIBLE);

    private final OptionalStubbingStrategy strategy;

    OptionalStubbingMode(OptionalStubbingStrategy strategy) {
        this.strategy = strategy;
    }

    OptionalStubbingStrategy getStrategy() {
        return strategy;
    }
}
