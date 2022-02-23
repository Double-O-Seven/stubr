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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

/**
 * A {@link StubbingSite} indicating that the current stubbing site is not known.
 * <p>
 * By default, a {@link ch.leadrian.stubr.core.Stubber} uses this site if none other is given.
 */
public final class UnknownStubbingSite implements StubbingSite {

    static final UnknownStubbingSite INSTANCE = new UnknownStubbingSite();

    private UnknownStubbingSite() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<? extends StubbingSite> getParent() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
