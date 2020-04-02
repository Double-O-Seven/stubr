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

package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * Stubbing site indicating that the current stubbing site is an array being filled with elements.
 */
public final class ArrayStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<ArrayStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ArrayStubbingSite.class)
            .compareAndHash(ArrayStubbingSite::getParent)
            .compareAndHash(ArrayStubbingSite::getComponentType)
            .build();

    private final StubbingSite parent;
    private final Class<?> componentType;

    ArrayStubbingSite(StubbingSite parent, Class<?> componentType) {
        requireNonNull(parent, "parent");
        requireNonNull(componentType, "type");
        this.parent = parent;
        this.componentType = componentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * Returns the component type of the array being stubbed.
     *
     * @return the component type of the array
     */
    public Class<?> getComponentType() {
        return componentType;
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
                .add("componentType", componentType)
                .toString();
    }

}
