package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.MethodMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.MethodParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class FactoryMethodStubber implements Stubber {

    private final MethodMatcher methodMatcher;
    private final Map<Class<?>, Method> factoryMethodsByClass = new ConcurrentHashMap<>();

    FactoryMethodStubber(MethodMatcher methodMatcher) {
        requireNonNull(methodMatcher, "methodMatcher");
        this.methodMatcher = methodMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getFactoryMethod(type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Method method = getFactoryMethod(type).orElseThrow(IllegalStateException::new);
        Object[] parameterValues = stream(method.getParameters())
                .map(parameter -> {
                    MethodParameterStubbingSite site = StubbingSites.methodParameter(context.getSite(), method, parameter);
                    return context.getStubber().stub(parameter.getParameterizedType(), site);
                })
                .toArray(Object[]::new);
        try {
            return method.invoke(null, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Optional<Method> getFactoryMethod(Type type) {
        Optional<Class<?>> targetClass = getRawType(type);
        return targetClass.flatMap(this::getFactoryMethod);
    }

    private Optional<Method> getFactoryMethod(Class<?> targetClass) {
        Method method = factoryMethodsByClass.computeIfAbsent(targetClass, clazz -> {
            List<Method> constructors = stream(clazz.getMethods())
                    .filter(m -> !m.isSynthetic() && !isPrivate(m.getModifiers()) && isStatic(m.getModifiers()))
                    .filter(m -> canReturn(m, targetClass))
                    .filter(methodMatcher::matches)
                    .collect(toList());
            if (constructors.size() == 1) {
                return constructors.get(0);
            }
            return null;
        });
        return Optional.ofNullable(method);
    }

    private boolean canReturn(Method method, Class<?> type) {
        return getRawType(method.getGenericReturnType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }

}