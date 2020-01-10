package ch.leadrian.stubr.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public interface TypeVisitor<T> {

    static <T> T accept(Type type, TypeVisitor<? extends T> visitor) {
        if (type instanceof Class) {
            return visitor.visit((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return visitor.visit((ParameterizedType) type);
        } else if (type instanceof WildcardType) {
            return visitor.visit((WildcardType) type);
        } else if (type instanceof TypeVariable) {
            return visitor.visit((TypeVariable<?>) type);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported type: %s", type));
        }
    }

    T visit(Class<?> clazz);

    T visit(ParameterizedType parameterizedType);

    T visit(WildcardType wildcardType);

    T visit(TypeVariable<?> typeVariable);

}
