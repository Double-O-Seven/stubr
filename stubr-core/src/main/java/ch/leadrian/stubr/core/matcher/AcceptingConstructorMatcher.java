package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.util.Types;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

final class AcceptingConstructorMatcher implements ConstructorMatcher {

    private final List<Class<?>> parameterTypes;

    AcceptingConstructorMatcher(Class<?>... parameterTypes) {
        requireNonNull(parameterTypes, "parameterTypes");
        this.parameterTypes = new ArrayList<>(asList(parameterTypes));
    }

    @Override
    public boolean matches(Constructor<?> constructor) {
        Parameter[] constructorParameters = constructor.getParameters();
        if (constructorParameters.length != parameterTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.size(); i++) {
            if (!isAssignable(constructorParameters[i], parameterTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isAssignable(Parameter parameter, Class<?> type) {
        return Types.getActualClass(parameter.getParameterizedType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }
}
