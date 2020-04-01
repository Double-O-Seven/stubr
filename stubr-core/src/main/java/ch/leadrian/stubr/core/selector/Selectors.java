package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Selector;

import java.util.Optional;

/**
 * Collection of factory methods for various default implementations of {@link ch.leadrian.stubr.core.Selector}.
 */
public final class Selectors {

    private Selectors() {
    }

    /**
     * Creates a {@link Selector} that selects a single value using the given {@link Matcher}.
     * <p>
     * The {@link Selector} returns {@link Optional#empty()} if zero or multiple values match the given {@link
     * Matcher}.
     *
     * @param matcher the {@link Matcher} used to select a value
     * @param <T>     type of selectable objects
     * @return a {@link Selector} that selects a single value using the given {@link Matcher}
     */
    public static <T> Selector<T> fromMatcher(Matcher<? super T> matcher) {
        return new SelectorFromMatcher<>(matcher);
    }

}
