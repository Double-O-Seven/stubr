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

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Field;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * A {@link StubbingSite} indicating that the current stubbing site is a field that is being injected with a stub
 * value.
 */
public final class InjectedFieldStubbingSite implements FieldStubbingSite {

    private static final EqualsAndHashCode<InjectedFieldStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(InjectedFieldStubbingSite.class)
            .compareAndHash(InjectedFieldStubbingSite::getParent)
            .compareAndHash(InjectedFieldStubbingSite::getField)
            .build();

    private final StubbingSite parent;
    private final Field field;

    InjectedFieldStubbingSite(StubbingSite parent, Field field) {
        requireNonNull(parent, "parent");
        requireNonNull(field, "field");
        this.parent = parent;
        this.field = field;
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
    public Field getField() {
        return field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return field.getName();
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
                .add("field", field)
                .toString();
    }

}
