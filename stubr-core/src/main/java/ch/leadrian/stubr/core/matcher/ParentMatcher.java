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

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;

import static java.util.Objects.requireNonNull;

final class ParentMatcher implements Matcher<StubbingSite> {

    private final Matcher<? super StubbingSite> delegate;

    ParentMatcher(Matcher<? super StubbingSite> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, StubbingSite value) {
        return value.getParent()
                .filter(parentSite -> delegate.matches(context, parentSite))
                .isPresent();
    }

}
