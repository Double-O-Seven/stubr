package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;
import static java.util.Arrays.asList;

final class AcceptingConstructorMatcher implements ConstructorMatcher {

    private final List<Class<?>> parameterTypes;

    AcceptingConstructorMatcher(Class<?>... parameterTypes) {
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
        return accept(parameter.getParameterizedType(), new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return clazz.isAssignableFrom(type);
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType, this);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType)
                        .filter(upperBound -> accept(upperBound, this))
                        .isPresent();
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return false;
            }
        });
    }
}
