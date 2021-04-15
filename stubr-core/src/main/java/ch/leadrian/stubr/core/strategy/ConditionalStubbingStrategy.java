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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ConditionalStubbingStrategy implements StubbingStrategy {

    private final StubbingStrategy delegate;
    private final Matcher<? super Type> typeMatcher;

    ConditionalStubbingStrategy(StubbingStrategy delegate, Matcher<? super Type> typeMatcher) {
        requireNonNull(delegate, "delegate");
        requireNonNull(typeMatcher, "typeMatcher");
        this.delegate = delegate;
        this.typeMatcher = typeMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return typeMatcher.matches(context, type) && delegate.accepts(context, type);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return delegate.stub(context, type);
    }

}
