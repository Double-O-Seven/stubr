package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.util.Objects.requireNonNull;

final class AcceptingConstructorMatcher implements ConstructorMatcher {

    private final List<Class<?>> parameterTypes;

    AcceptingConstructorMatcher(List<Class<?>> parameterTypes) {
        requireNonNull(parameterTypes, "parameterTypes");
        this.parameterTypes = ImmutableList.copyOf(parameterTypes);
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
        return getRawType(parameter.getParameterizedType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }
}
