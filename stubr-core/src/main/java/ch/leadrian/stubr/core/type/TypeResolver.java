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

package ch.leadrian.stubr.core.type;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.internal.com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * A helper class that is able to resolve generic types such as type variables given a concrete type.
 */
public final class TypeResolver {

    private static final EqualsAndHashCode<TypeResolver> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(TypeResolver.class)
            .compareAndHash(resolver -> resolver.type)
            .build();

    private final TypeToken<?> type;

    /**
     * Returns a new {@code TypeResolver} that uses the given {@code typeLiteral} to resolve other types.
     *
     * @param typeLiteral the concrete type literal used to resolver other types
     * @return a new {@code TypeResolver} that uses the given {@code typeLiteral} to resolve other types
     * @see TypeResolver#using(Type)
     */
    public static TypeResolver using(TypeLiteral<?> typeLiteral) {
        return using(typeLiteral.getType());
    }

    /**
     * Returns a new {@code TypeResolver} that uses the given {@code type} to resolve other types.
     *
     * @param type the concrete type used to resolver other types
     * @return a new {@code TypeResolver} that uses the given {@code type} to resolve other types
     */
    public static TypeResolver using(Type type) {
        return new TypeResolver(type);
    }

    private TypeResolver(Type type) {
        requireNonNull(type, "type");
        this.type = TypeToken.of(type);
    }

    /**
     * Resolves a possibly generic type to a concrete type.
     *
     * @param genericType the possible generic type which should be resolved to a concrete type
     * @return the resolved type
     */
    public Type resolve(Type genericType) {
        return type.resolveType(genericType).getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        return EQUALS_AND_HASH_CODE.equals(this, o);
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
                .add("type", type)
                .toString();
    }

}
