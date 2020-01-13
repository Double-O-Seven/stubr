package ch.leadrian.stubr.core.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeLiterals {

    private TypeLiterals() {
    }

    public static Type getTypeArgument(TypeLiteral<?> typeLiteral, int index) {
        Type type = typeLiteral.getType();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalStateException();
        }
        return ((ParameterizedType) type).getActualTypeArguments()[index];
    }
}
