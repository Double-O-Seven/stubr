package ch.leadrian.stubr.core.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;

public final class Types {

    private Types() {
    }

    public static Optional<Class<?>> getActualClass(Type type) {
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
                return getMostSpecificType(wildcardType).flatMap(type -> accept(type, this));
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

    public static Optional<Type> getLowerBound(WildcardType type) {
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 1) {
            return Optional.of(lowerBounds[0]);
        }
        return Optional.empty();
    }

    public static Optional<Type> getOnlyUpperBound(WildcardType type) {
        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length == 1) {
            return Optional.of(upperBounds[0]);
        }
        return Optional.empty();
    }

    public static Optional<Type> getMostSpecificType(WildcardType wildcardType) {
        Optional<Type> lowerBound = getLowerBound(wildcardType);
        if (lowerBound.isPresent()) {
            return lowerBound;
        }
        return getOnlyUpperBound(wildcardType);
    }
}
