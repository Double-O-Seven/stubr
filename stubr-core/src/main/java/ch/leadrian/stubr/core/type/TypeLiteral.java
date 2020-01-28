package ch.leadrian.stubr.core.type;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;

@SuppressWarnings("unused")
public abstract class TypeLiteral<T> {

    private static final EqualsAndHashCode<TypeLiteral> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(TypeLiteral.class)
            .compareAndHash(TypeLiteral::getType)
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

    public Type getType() {
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

    @Override
    public String toString() {
        return toStringHelper(TypeLiteral.class)
                .add("type", type)
                .toString();
    }

}
