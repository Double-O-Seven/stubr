/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
