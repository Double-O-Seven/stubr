package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;
import ch.leadrian.stubr.core.type.TypeLiteral;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;

/**
 * Collection of factory methods for various implementations of default {@link StubbingSite}s.
 */
public final class StubbingSites {

    private StubbingSites() {
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is an array being filled with elements.
     *
     * @param parent        the parent site, must not be {@code null}
     * @param componentType the component type of the array
     * @return an array stubbing site
     * @see ArrayStubbingSite
     * @see StubbingStrategies#array()
     */
    public static ArrayStubbingSite array(StubbingSite parent, Class<?> componentType) {
        return new ArrayStubbingSite(parent, componentType);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a constructor parameter.
     *
     * @param parent      the parent site, must not be {@code null}
     * @param constructor the constructor
     * @param parameter   the parameter for which a stub value is requested
     * @return a constructor stubbing site
     * @see ConstructorParameterStubbingSite
     * @see StubbingStrategies#constructor()
     */
    public static ConstructorParameterStubbingSite constructorParameter(StubbingSite parent, Constructor<?> constructor, Parameter parameter) {
        return new ConstructorParameterStubbingSite(parent, constructor, parameter);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a constructor parameter.
     *
     * @param parent         the parent site, must not be {@code null}
     * @param constructor    the constructor
     * @param parameterIndex the parameter index of the parameter for which a stub value is requested
     * @return a constructor stubbing site
     * @see ConstructorParameterStubbingSite
     * @see StubbingStrategies#constructor()
     */
    public static ConstructorParameterStubbingSite constructorParameter(StubbingSite parent, Constructor<?> constructor, int parameterIndex) {
        return constructorParameter(parent, constructor, constructor.getParameters()[parameterIndex]);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a method parameter.
     *
     * @param parent    the parent site, must not be {@code null}
     * @param method    the method
     * @param parameter the parameter for which a stub value is requested
     * @return a method stubbing site
     * @see MethodParameterStubbingSite
     * @see StubbingStrategies#factoryMethod()
     */
    public static MethodParameterStubbingSite methodParameter(StubbingSite parent, Method method, Parameter parameter) {
        return new MethodParameterStubbingSite(parent, method, parameter);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a method parameter.
     *
     * @param parent         the parent site, must not be {@code null}
     * @param method         the method
     * @param parameterIndex the parameter index of the parameter for which a stub value is requested
     * @return a method stubbing site
     * @see MethodParameterStubbingSite
     * @see StubbingStrategies#factoryMethod()
     */
    public static MethodParameterStubbingSite methodParameter(StubbingSite parent, Method method, int parameterIndex) {
        return methodParameter(parent, method, method.getParameters()[parameterIndex]);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a method return.
     *
     * @param parent the parent site, must not be {@code null}
     * @param method the method
     * @return a method return stubbing site
     * @see MethodReturnValueStubbingSite
     * @see StubbingStrategies#proxy()
     */
    public static MethodReturnValueStubbingSite methodReturnValue(StubbingSite parent, Method method) {
        return new MethodReturnValueStubbingSite(parent, method);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is the stubbing of a parameterized
     * type.
     * <p>
     * Examples for this are {@link StubbingStrategies#optional()} or {@link StubbingStrategies#collection(Class,
     * Supplier)}
     *
     * @param parent         the parent site, must not be {@code null}
     * @param type           the parameterized type for which stub values for its type arguments are requested
     * @param parameterIndex the index of the type argument
     * @return a parameterized type site
     * @see MethodReturnValueStubbingSite
     * @see StubbingStrategies#optional()
     * @see StubbingStrategies#collection(Class, Supplier)
     */
    public static ParameterizedTypeStubbingSite parameterizedType(StubbingSite parent, ParameterizedType type, int parameterIndex) {
        return new ParameterizedTypeStubbingSite(parent, type, parameterIndex);
    }

    /**
     * Returns {@link StubbingSite} indicating that the current stubbing site is not known.
     * <p>
     * By default, a {@link ch.leadrian.stubr.core.Stubber} uses this site if none other is given.
     *
     * @return an unknown stubbing site
     * @see ch.leadrian.stubr.core.Stubber#tryToStub(Class)
     * @see ch.leadrian.stubr.core.Stubber#tryToStub(TypeLiteral)
     * @see ch.leadrian.stubr.core.Stubber#stub(Class)
     * @see ch.leadrian.stubr.core.Stubber#stub(TypeLiteral)
     */
    public static UnknownStubbingSite unknown() {
        return UnknownStubbingSite.INSTANCE;
    }

}
