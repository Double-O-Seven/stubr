package ch.leadrian.stubr.core;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("UnstableApiUsage")
public final class TypeTokens {

    private TypeTokens() {
    }

    public static Type getTypeArgument(TypeToken<?> token, int index) {
        return ((ParameterizedType) token.getType()).getActualTypeArguments()[index];
    }
}
