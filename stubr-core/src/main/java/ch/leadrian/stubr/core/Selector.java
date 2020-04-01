package ch.leadrian.stubr.core;

import java.util.List;
import java.util.Optional;

import static ch.leadrian.stubr.core.selector.Selectors.compose;

/**
 * Class used to select zero or one value from a given list of values.
 *
 * @param <T> type of selectable objects
 */
@FunctionalInterface
public interface Selector<T> {

    /**
     * @param context the {@link StubbingContext} in which a value must be selected
     * @param values  the values from which at most one is selected
     * @return zero or one value from the given {@code values}
     */
    Optional<T> select(StubbingContext context, List<? extends T> values);

    /**
     * Creates a composite {@code Selector} that tries to select a value using {@code other} if {@code this} has
     * returned an empty value.
     *
     * @param fallback the fallback selector
     * @return a composite selector that falls back to {@code fallback} if {@code this} fails to select a value
     */
    default Selector<T> orElse(Selector<T> fallback) {
        return compose(this, fallback);
    }

}
