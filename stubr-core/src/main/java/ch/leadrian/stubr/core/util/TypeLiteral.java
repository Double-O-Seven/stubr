package ch.leadrian.stubr.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unused")
public abstract class TypeLiteral<T> {

    private final Type type;

    protected TypeLiteral() {
        Type superclass = getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new IllegalStateException("Superclass must be parameterized");
        }
        Type[] typeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
        if (typeArguments.length != 1) {
            throw new IllegalStateException("Expected exactly one type argument");
        }
        type = typeArguments[0];
    }

    public final Type getType() {
        return type;
    }
}
