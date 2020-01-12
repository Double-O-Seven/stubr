package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

import static java.util.Objects.requireNonNull;

public final class StubbingSites {

    private StubbingSites() {
    }

    public static ConstructorParameterStubbingSite constructorParameter(StubbingSite parent, Constructor<?> constructor, Parameter parameter) {
        requireNonNull(constructor, "constructor may not be null");
        requireNonNull(parameter, "parameter may not be null");
        return new ConstructorParameterStubbingSite(parent, constructor, parameter);
    }

    public static MethodParameterStubbingSite methodParameter(StubbingSite parent, Method method, Parameter parameter) {
        requireNonNull(method, "method may not be null");
        requireNonNull(parameter, "parameter may not be null");
        return new MethodParameterStubbingSite(parent, method, parameter);
    }

    public static MethodReturnValueStubbingSite methodReturnValue(StubbingSite parent, Method method) {
        requireNonNull(method, "method may not be null");
        return new MethodReturnValueStubbingSite(parent, method);
    }

    public static ParameterizedTypeStubbingSite parameterizedType(StubbingSite parent, ParameterizedType type, int parameterIndex) {
        requireNonNull(parent, "parent may not be null");
        requireNonNull(type, "type may not be null");
        return new ParameterizedTypeStubbingSite(parent, type, parameterIndex);
    }

    public static UnknownStubbingSite unknown() {
        return UnknownStubbingSite.INSTANCE;
    }
}
