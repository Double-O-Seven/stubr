package ch.leadrian.stubr.core.type;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * A class representing a generic type {@link T}. Since only raw class literals are supported out of the box by Java,
 * this class can be used to access a generic {@link Type} during runtime.
 * <p>
 * The class is abstract since a concrete implementation (usually anonynmous class) must be created in order to be able
 * to access the actual type {@link T}.
 * <p>
 * To get the actual type of {@code List<String>} for example, an anonymous class may be created for example:
 * <pre>
 * Type listType = new TypeLiteral<List<String>>() {}.getType();
 * </pre>
 *
 * @param <T> the generic type
 */
@SuppressWarnings("unused")
public abstract class TypeLiteral<T> {

    private static final EqualsAndHashCode<TypeLiteral> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(TypeLiteral.class)
            .compareAndHash(TypeLiteral::getType)
            .build();

    private final Type type;

    /**
     * The default constructor used to infer the actual type of the type literal.
     */
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

    /**
     * Returns the actual {@link Type} instance of {@link T}.
     *
     * @return the actual {@link Type} instance of {@link T}
     */
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(TypeLiteral.class)
                .add("type", type)
                .toString();
    }

}
