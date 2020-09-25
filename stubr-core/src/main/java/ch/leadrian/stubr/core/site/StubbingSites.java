/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;
import ch.leadrian.stubr.core.type.TypeLiteral;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

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
        int index = asList(constructor.getParameters()).indexOf(parameter);
        if (index == -1) {
            throw new IllegalArgumentException(String.format("%s is not a parameter of %s", parameter, constructor));
        }
        return new ConstructorParameterStubbingSite(parent, constructor, parameter, index);
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
        return new ConstructorParameterStubbingSite(parent, constructor, constructor.getParameters()[parameterIndex], parameterIndex);
    }

    /**
     * Returns a {@link StubbingSite} indicating that the current stubbing site is a field that is being injected with a
     * stub value.
     *
     * @param parent the parent site, must not be {@code null}
     * @param field  the field that was set to a stub value
     * @return a field stubbing site
     * @see StubbingStrategies#fieldInjection(Matcher)
     */
    public static InjectedFieldStubbingSite injectedField(StubbingSite parent, Field field) {
        return new InjectedFieldStubbingSite(parent, field);
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
        int index = asList(method.getParameters()).indexOf(parameter);
        if (index == -1) {
            throw new IllegalArgumentException(String.format("%s is not a parameter of %s", parameter, method));
        }
        return new MethodParameterStubbingSite(parent, method, parameter, index);
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
        return new MethodParameterStubbingSite(parent, method, method.getParameters()[parameterIndex], parameterIndex);
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
     * @param parent            the parent site, must not be {@code null}
     * @param type              the parameterized type for which stub values for its type arguments are requested
     * @param typeArgumentIndex the index of the type argument
     * @return a parameterized type site
     * @see MethodReturnValueStubbingSite
     * @see StubbingStrategies#optional()
     * @see StubbingStrategies#collection(Class, Supplier)
     */
    public static ParameterizedTypeStubbingSite parameterizedType(StubbingSite parent, ParameterizedType type, int typeArgumentIndex) {
        return new ParameterizedTypeStubbingSite(parent, type, typeArgumentIndex);
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
