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

package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.util.Objects;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Stubbing site indicating that the current stubbing site is a memoizing one.
 *
 * @see ch.leadrian.stubr.core.strategy.StubbingStrategies#memoized(StubbingStrategy)
 */
public final class MemoizingStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<MemoizingStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MemoizingStubbingSite.class)
            .compareAndHash(MemoizingStubbingSite::getParent)
            .build();

    private final StubbingSite parent;

    MemoizingStubbingSite(StubbingSite parent) {
        Objects.requireNonNull(parent, "parent");
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("parent", parent)
                .toString();
    }

}
