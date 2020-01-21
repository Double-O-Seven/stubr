package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.List;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.util.Objects.requireNonNull;

final class ParameterTypesMatcher<T extends Executable> implements Matcher<T> {

    private final List<Class<?>> parameterTypes;

    ParameterTypesMatcher(Class<?>... parameterTypes) {
        requireNonNull(parameterTypes, "parameterTypes");
        this.parameterTypes = ImmutableList.copyOf(parameterTypes);
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        Parameter[] parameters = value.getParameters();
        if (parameters.length != parameterTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.size(); i++) {
            if (!isAssignable(parameters[i], parameterTypes.get(i))) {
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
