package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.MethodMatcher;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.util.Types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

final class FactoryMethodStubber implements Stubber {

    private final MethodMatcher methodMatcher;
    private final Map<Class<?>, Method> constructorsByClass = new ConcurrentHashMap<>();

    FactoryMethodStubber(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    @Override
    public boolean accepts(Type type) {
        return getFactoryMethod(type).isPresent();
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        Method method = getFactoryMethod(type).orElseThrow(IllegalStateException::new);
        Object[] parameterValues = stream(method.getGenericParameterTypes())
                .map(rootStubber::stub)
                .toArray(Object[]::new);
        try {
            return method.invoke(null, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Optional<Method> getFactoryMethod(Type type) {
        Optional<Class<?>> targetClass = Types.getActualClass(type);
        return targetClass.flatMap(this::getFactoryMethod);
    }

    private Optional<Method> getFactoryMethod(Class<?> targetClass) {
        Method method = constructorsByClass.computeIfAbsent(targetClass, clazz -> {
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
        return Types.getActualClass(method.getGenericReturnType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }

}