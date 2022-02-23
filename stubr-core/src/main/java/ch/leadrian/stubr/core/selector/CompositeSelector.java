/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.internal.com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class CompositeSelector<T> implements Selector<T> {

    private final List<Selector<T>> selectors;

    CompositeSelector(List<Selector<T>> selectors) {
        requireNonNull(selectors, "selectors");
        this.selectors = ImmutableList.copyOf(selectors);
    }

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        return selectors.stream().map(selector -> selector.select(context, values))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
