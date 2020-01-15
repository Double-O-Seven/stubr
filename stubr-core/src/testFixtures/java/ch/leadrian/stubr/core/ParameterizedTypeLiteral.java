package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ParameterizedTypeLiteral<T> extends TypeLiteral<T> {

    protected ParameterizedTypeLiteral() {
        if (!(super.getType() instanceof ParameterizedType)) {
            throw new IllegalStateException("Expected ParameterizedType literal");
        }
    }

    @Override
    public ParameterizedType getType() {
        return (ParameterizedType) super.getType();
    }

    public Type getActualTypeArgument(int index) {
        return getType().getActualTypeArguments()[index];
    }
}
