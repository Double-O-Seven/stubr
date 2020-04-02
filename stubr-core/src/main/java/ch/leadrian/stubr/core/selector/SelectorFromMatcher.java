/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
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
import ch.leadrian.stubr.core.StubbingContext;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class SelectorFromMatcher<T> implements Selector<T> {

    private final Matcher<? super T> matcher;

    SelectorFromMatcher(Matcher<? super T> matcher) {
        requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        List<? extends T> matchingValues = values.stream()
                .filter(value -> matcher.matches(context, value))
                .collect(toList());
        return Optional.ofNullable(matchingValues.size() == 1 ? matchingValues.get(0) : null);
    }

}
