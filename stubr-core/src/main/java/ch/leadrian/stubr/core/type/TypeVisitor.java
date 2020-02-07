package ch.leadrian.stubr.core.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * A visitor interface for visiting {@link Type}s and its various implementations.
 *
 * @param <T> the type of the result of the visitation
 */
public interface TypeVisitor<T> {

    /**
     * Since {@link Type} cannot be extended to have the {@code accept} of the visitor pattern, a static variant is
     * provided.
     * <p>
     * The following implementations/subtypes of {@link Type} are supported:
     * <ul>
     * <li>{@link Class}</li>
     * <li>{@link ParameterizedType}</li>
     * <li>{@link WildcardType}</li>
     * <li>{@link TypeVariable}</li>
     * <li>{@link GenericArrayType}</li>
     * </ul>
     *
     * @param type    the type to be visited
     * @param visitor the visitor visiting the type
     * @param <T>     the  type of the result of the visitation
     * @return the result of the visitation
     */
    static <T> T accept(Type type, TypeVisitor<? extends T> visitor) {
        if (type instanceof Class) {
            return visitor.visit((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return visitor.visit((ParameterizedType) type);
        } else if (type instanceof WildcardType) {
            return visitor.visit((WildcardType) type);
        } else if (type instanceof TypeVariable) {
            return visitor.visit((TypeVariable<?>) type);
        } else if (type instanceof GenericArrayType) {
            return visitor.visit((GenericArrayType) type);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported type: %s", type));
        }
    }

    /**
     * Visit method for visiting a {@link Class}.
     *
     * @param clazz the visited type
     * @return the result of the visitation
     */
    T visit(Class<?> clazz);

    /**
     * Visit method for visiting a {@link ParameterizedType}.
     *
     * @param parameterizedType the visited type
     * @return the result of the visitation
     */
    T visit(ParameterizedType parameterizedType);

    /**
     * Visit method for visiting a {@link WildcardType}.
     *
     * @param wildcardType the visited type
     * @return the result of the visitation
     */
    T visit(WildcardType wildcardType);

    /**
     * Visit method for visiting a {@link TypeVariable}.
     *
     * @param typeVariable the visited type
     * @return the result of the visitation
     */
    T visit(TypeVariable<?> typeVariable);

    /**
     * Visit method for visiting a {@link GenericArrayType}.
     *
     * @param genericArrayType the visited type
     * @return the result of the visitation
     */
    T visit(GenericArrayType genericArrayType);

}
