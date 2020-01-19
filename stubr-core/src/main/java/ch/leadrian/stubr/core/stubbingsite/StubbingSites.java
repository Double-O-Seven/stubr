package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

public final class StubbingSites {

    private StubbingSites() {
    }

    public static ArrayStubbingSite array(StubbingSite parent, Class<?> componentType) {
        return new ArrayStubbingSite(parent, componentType);
    }

    public static ConstructorParameterStubbingSite constructorParameter(StubbingSite parent, Constructor<?> constructor, Parameter parameter) {
        return new ConstructorParameterStubbingSite(parent, constructor, parameter);
    }

    public static ConstructorParameterStubbingSite constructorParameter(StubbingSite parent, Constructor<?> constructor, int parameterIndex) {
        return new ConstructorParameterStubbingSite(parent, constructor, constructor.getParameters()[parameterIndex]);
    }

    public static MethodParameterStubbingSite methodParameter(StubbingSite parent, Method method, Parameter parameter) {
        return new MethodParameterStubbingSite(parent, method, parameter);
    }

    public static MethodParameterStubbingSite methodParameter(StubbingSite parent, Method method, int parameterIndex) {
        return new MethodParameterStubbingSite(parent, method, method.getParameters()[parameterIndex]);
    }

    public static MethodReturnValueStubbingSite methodReturnValue(StubbingSite parent, Method method) {
        return new MethodReturnValueStubbingSite(parent, method);
    }

    public static ParameterizedTypeStubbingSite parameterizedType(StubbingSite parent, ParameterizedType type, int parameterIndex) {
        return new ParameterizedTypeStubbingSite(parent, type, parameterIndex);
    }

    public static UnknownStubbingSite unknown() {
        return UnknownStubbingSite.INSTANCE;
    }
}
