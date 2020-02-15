package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;

/**
 * Interface to supplied a stub value for a given context and sequence number.
 *
 * @param <T> type of the supplied value
 */
@FunctionalInterface
public interface StubValueSupplier<T> {

    /**
     * Returns a stub value for the given context and sequence number.
     *
     * @param context        the {@link StubbingContext}
     * @param sequenceNumber the sequence number
     * @return a stub value
     */
    T get(StubbingContext context, int sequenceNumber);

}
