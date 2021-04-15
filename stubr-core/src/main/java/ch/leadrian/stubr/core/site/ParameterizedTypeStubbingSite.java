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

package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * A {@link StubbingSite} indicating that the current stubbing site is the stubbing of a parameterized type.
 * <p>
 * Examples for this are {@link StubbingStrategies#optional()} or {@link StubbingStrategies#collection(Class,
 * java.util.function.Supplier)}
 */
public final class ParameterizedTypeStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<ParameterizedTypeStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ParameterizedTypeStubbingSite.class)
            .compareAndHash(ParameterizedTypeStubbingSite::getParent)
            .compareAndHash(ParameterizedTypeStubbingSite::getType)
            .compareAndHashPrimitive(ParameterizedTypeStubbingSite::getTypeArgumentIndex)
            .build();

    private final StubbingSite parent;
    private final ParameterizedType type;
    private final int typeArgumentIndex;

    ParameterizedTypeStubbingSite(StubbingSite parent, ParameterizedType type, int typeArgumentIndex) {
        requireNonNull(parent, "parent");
        requireNonNull(type, "type");
        this.parent = parent;
        this.type = type;
        this.typeArgumentIndex = typeArgumentIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * Returns the parameterized type of the stub value for which a stub value for a type argument is requested.
     *
     * @return the parameterized type
     */
    public ParameterizedType getType() {
        return type;
    }

    /**
     * Returns the index of the type argument for which a stub value is requested.
     *
     * @return the index of the type argument
     */
    public int getTypeArgumentIndex() {
        return typeArgumentIndex;
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
                .add("type", type)
                .add("typeArgumentIndex", typeArgumentIndex)
                .toString();
    }

}
