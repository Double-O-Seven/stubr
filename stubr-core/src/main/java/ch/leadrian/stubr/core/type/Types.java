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

package ch.leadrian.stubr.core.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

import static ch.leadrian.stubr.core.type.TypeVisitor.accept;

/**
 * Utility class for processing {@link Type}s.
 */
public final class Types {

    private Types() {
    }

    /**
     * Returns the raw {@link Class} representation for the given type, if a raw type can be inferred.
     * <p>
     * If {@code type} is a {@link Class}, the type itself will be returned.
     * <p>
     * If {@code type} is a {@link ParameterizedType}, the raw type will be recursively inferred from {@link
     * ParameterizedType#getRawType()}.
     * <p>
     * If {@code type} is a {@link WildcardType}, the raw type will be recursively inferred from the wildcard's bound.
     * <p>
     * If {@code type} is anything else, {@link Optional#empty()} will be returned.
     *
     * @param type the from which an actual {@link Class} should be inferred
     * @return Am {@link Optional} the actual raw type if a {@link Class} can be derived from the given {@link Type},
     * else {@link Optional#empty()}
     */
    public static Optional<Class<?>> getRawType(Type type) {
        return accept(type, new TypeVisitor<Optional<Class<?>>>() {

            @Override
            public Optional<Class<?>> visit(Class<?> clazz) {
                return Optional.of(clazz);
            }

            @Override
            public Optional<Class<?>> visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getRawType(), this);
            }

            @Override
            public Optional<Class<?>> visit(WildcardType wildcardType) {
                return getBound(wildcardType).flatMap(type -> accept(type, this));
            }

            @Override
            public Optional<Class<?>> visit(TypeVariable<?> typeVariable) {
                return Optional.empty();
            }

            @Override
            public Optional<Class<?>> visit(GenericArrayType genericArrayType) {
                return Optional.empty();
            }
        });
    }

    /**
     * Returns the lower bound of the given {@link WildcardType} if there is exactly one lower bound, else {@link
     * Optional#empty()}.
     *
     * @param type the wildcard type
     * @return the lower bound of the given {@code type}
     */
    public static Optional<Type> getLowerBound(WildcardType type) {
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 1) {
            return Optional.of(lowerBounds[0]);
        }
        return Optional.empty();
    }

    /**
     * Returns the upper bound of the given {@link WildcardType} if there is exactly one upper bound, else {@link
     * Optional#empty()}.
     *
     * @param type the wildcard type
     * @return the upper bound of the given {@code type}
     */
    public static Optional<Type> getOnlyUpperBound(WildcardType type) {
        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length == 1) {
            return Optional.of(upperBounds[0]);
        }
        return Optional.empty();
    }

    /**
     * Returns the lower bound of the given {@code wildcardType} if present, else if the upper bound.
     *
     * @param wildcardType the wildcard type
     * @return the lower bound of the given {@code wildcardType} if present, else if the upper bound
     */
    public static Optional<Type> getBound(WildcardType wildcardType) {
        Optional<Type> lowerBound = getLowerBound(wildcardType);
        if (lowerBound.isPresent()) {
            return lowerBound;
        }
        return getOnlyUpperBound(wildcardType);
    }

}
