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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;
import java.util.function.Consumer;

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
     * @param type the type from which an actual {@link Class} should be inferred
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
                return getRawType(genericArrayType.getGenericComponentType())
                        .map(componentType -> Array.newInstance(componentType, 0).getClass());
            }
        });
    }

    /**
     * Returns the raw {@link Class} representation for the given type, if a raw type can be inferred.
     * <p>
     * If {@code typeLiteral.getType()} returns a {@link Class}, the type itself will be returned.
     * <p>
     * If {@code typeLiteral.getType()} returns a {@link ParameterizedType}, the raw type will be recursively inferred
     * from {@link ParameterizedType#getRawType()}.
     * <p>
     * If {@code typeLiteral.getType()} returns a {@link WildcardType}, the raw type will be recursively inferred from
     * the wildcard's bound.
     * <p>
     * If {@code typeLiteral.getType()} returns anything else, {@link Optional#empty()} will be returned.
     *
     * @param <T>         the type represented by the type literal
     * @param typeLiteral the type literal from which an actual {@link Class} should be inferred
     * @return Am {@link Optional} the actual raw type if a {@link Class} can be derived from the given {@link Type},
     * else {@link Optional#empty()}
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> getRawType(TypeLiteral<T> typeLiteral) {
        return getRawType(typeLiteral.getType()).map(type -> (Class<T>) type);
    }

    /**
     * Returns the lower bound of the given {@link WildcardType} if there is exactly one lower bound, else {@link
     * Optional#empty()}.
     *
     * @param type the wildcard type
     * @return the lower bound of the given {@code type}
     */
    public static Optional<Type> getLowerBound(Type type) {
        if (type instanceof WildcardType) {
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            if (lowerBounds.length == 1) {
                return Optional.of(lowerBounds[0]);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the upper bound of the given {@link WildcardType} or {@link TypeVariable} if there is exactly one upper
     * bound, else {@link Optional#empty()}.
     *
     * @param type the type
     * @return the upper bound of the given {@code type}
     */
    public static Optional<Type> getOnlyUpperBound(Type type) {
        return accept(type, new TypeVisitor<Optional<Type>>() {

            @Override
            public Optional<Type> visit(Class<?> clazz) {
                return Optional.empty();
            }

            @Override
            public Optional<Type> visit(ParameterizedType parameterizedType) {
                return Optional.empty();
            }

            @Override
            public Optional<Type> visit(WildcardType wildcardType) {
                Type[] upperBounds = wildcardType.getUpperBounds();
                if (upperBounds.length == 1) {
                    return Optional.of(upperBounds[0]);
                }
                return Optional.empty();
            }

            @Override
            public Optional<Type> visit(TypeVariable<?> typeVariable) {
                Type[] upperBounds = typeVariable.getBounds();
                if (upperBounds.length == 1) {
                    return Optional.of(upperBounds[0]);
                }
                return Optional.empty();
            }

            @Override
            public Optional<Type> visit(GenericArrayType genericArrayType) {
                return Optional.empty();
            }
        });
    }

    /**
     * Returns the lower bound of the given {@code type} if present, else if the upper bound.
     *
     * @param type the type
     * @return the lower bound of the given {@code type} if present, else if the upper bound
     */
    public static Optional<Type> getBound(Type type) {
        Optional<Type> lowerBound = getLowerBound(type);
        if (lowerBound.isPresent()) {
            return lowerBound;
        }
        return getOnlyUpperBound(type);
    }

    /**
     * Removes the wildcard from the given {@code type}.
     *
     * @param type the type that may contain a wildcard
     * @return the type without the wildcards
     * @throws IllegalArgumentException if the the given {@code type} is a {@link WildcardType} with multiple bounds
     */
    public static Type trimWildcard(Type type) {
        return accept(type, new TypeVisitor<Type>() {

            @Override
            public Type visit(Class<?> clazz) {
                return clazz;
            }

            @Override
            public Type visit(ParameterizedType parameterizedType) {
                return parameterizedType;
            }

            @Override
            public Type visit(WildcardType wildcardType) {
                return getBound(type).orElseThrow(() -> new IllegalArgumentException("Cannot trim wildcard type that does not have exactly one bound: " + type));
            }

            @Override
            public Type visit(TypeVariable<?> typeVariable) {
                return getBound(type).orElseThrow(() -> new IllegalArgumentException("Cannot trim type variable that does not have exactly one bound: " + type));
            }

            @Override
            public Type visit(GenericArrayType genericArrayType) {
                return genericArrayType;
            }
        });
    }

    /**
     * Visits all superclasses and interfaces of the given {@code type}. First, all superclasses are visited
     * recursively, followed by implemented interfaces. Interfaces declared on different may be visited multiple times.
     *
     * @param type    the type to visit
     * @param visitor the visitor function applied to the supertypes
     */
    public static void visitTypeHierarchy(Class<?> type, Consumer<? super Class<?>> visitor) {
        if (type == null) {
            return;
        }

        visitor.accept(type);
        visitTypeHierarchy(type.getSuperclass(), visitor);
        for (Class<?> interfaceType : type.getInterfaces()) {
            visitTypeHierarchy(interfaceType, visitor);
        }
    }

}
