package ch.leadrian.stubr.core.type;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;

@SuppressWarnings("unused")
public abstract class TypeLiteral<T> {

    private static final EqualsAndHashCode<TypeLiteral> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(TypeLiteral.class)
            .compare(TypeLiteral::getType)
            .build();

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

    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }
}
