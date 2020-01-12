package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

    public static UnknownStubbingSite unknown() {
        return UnknownStubbingSite.INSTANCE;
    }
}
