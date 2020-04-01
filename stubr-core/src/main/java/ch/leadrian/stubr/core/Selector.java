package ch.leadrian.stubr.core;

import java.util.List;
import java.util.Optional;

/**
 * Class used to select zero or one value from a given list of values.
 *
 * @param <T> type of selectable objects
 */
public interface Selector<T> {

    /**
     * @param context the {@link StubbingContext} in which a value must be selected
     * @param values  the values from which at most one is selected
     * @return zero or one value from the given {@code values}
     */
    Optional<T> select(StubbingContext context, List<? extends T> values);

}
