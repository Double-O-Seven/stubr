package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public interface Stubber {

    boolean accepts(Type type);

    default boolean accepts(Parameter parameter) {
        return accepts(parameter.getParameterizedType());
    }

    Object stub(RootStubber rootStubber, Type type);

    default Object stub(RootStubber rootStubber, Parameter parameter) {
        return stub(rootStubber, parameter.getParameterizedType());
    }

}
