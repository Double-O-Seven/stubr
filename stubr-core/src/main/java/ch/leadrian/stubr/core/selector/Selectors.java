/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.util.Optional;
import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Collection of factory methods for various default implementations of {@link ch.leadrian.stubr.core.Selector}.
 *
 * @see StubbingStrategies#constructor
 * @see StubbingStrategies#factoryMethod
 * @see StubbingStrategies#enumValue
 */
public final class Selectors {

    private static final class RandomHolder {

        static final Random INSTANCE = new Random(System.currentTimeMillis());

    }

    private Selectors() {
    }

    /**
     * Creates a {@link Selector} that selects a single value using the given {@link Matcher}.
     * <p>
     * The {@link Selector}s will be used in the given order and the first non-empty value returned by a {@link
     * Selector} will be returned.
     *
     * @param selectors the selectors used to select the first best value
     * @param <T>       type of selectable objects
     * @return a {@link Selector} that returns the first selected value returned by a {@link Selector} in {@code
     * selectors}
     */
    @SafeVarargs
    public static <T> Selector<T> compose(Selector<T>... selectors) {
        return new CompositeSelector<>(asList(selectors));
    }

    /**
     * Creates a {@link Selector} that selects the first non-null element.
     *
     * @param <T> type of selectable objects
     * @return a {@link Selector} that selects the first non-null element
     */
    public static <T> Selector<T> first() {
        return new FirstElementSelector<>();
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

    /**
     * Creates a {@link Selector} that randomly selects an element from the given list of values.
     * <p>
     * The selected index is determined by the given {@code random} object.
     *
     * @param random the random
     * @param <T>    type of selectable objects
     * @return a {@link Selector} that randomly selects an element from the given list of values
     */
    public static <T> Selector<T> random(Random random) {
        return new RandomSelector<>(random);
    }

    /**
     * Creates a {@link Selector} that randomly selects an element from the given list of values.
     * <p>
     * The selected index is determined by {@link Random} instance created with the given {@code seed}.
     *
     * @param seed the seed used to create a the {@link Random}
     * @param <T>  type of selectable objects
     * @return a {@link Selector} that randomly selects an element from the given list of values
     */
    public static <T> Selector<T> random(long seed) {
        return random(new Random(seed));
    }

    /**
     * Creates a {@link Selector} that randomly selects an element from the given list of values.
     * <p>
     * The selected index is determined by {@link Random} instance created with {@link System#currentTimeMillis()} as
     * seed.
     *
     * @param <T> type of selectable objects
     * @return a {@link Selector} that randomly selects an element from the given list of values
     */
    public static <T> Selector<T> random() {
        return random(RandomHolder.INSTANCE);
    }

}
