package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.ConstructorParameterStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPrivate;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

final class ConstructorStubbingStrategy implements StubbingStrategy {

    private final Selector<Constructor<?>> constructorSelector;
    private final Map<Class<?>, Optional<Constructor<?>>> constructorsByClass = new ConcurrentHashMap<>();

    ConstructorStubbingStrategy(Selector<Constructor<?>> constructorSelector) {
        requireNonNull(constructorSelector, "constructorSelector");
        this.constructorSelector = constructorSelector;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getConstructor(context, type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Constructor<?> constructor = getConstructor(context, type)
                .orElseThrow(() -> new StubbingException("No matching constructor found", context.getSite(), type));
        Object[] parameterValues = stub(context, constructor);
        return invokeConstructor(constructor, parameterValues);
    }

    private Object invokeConstructor(Constructor<?> constructor, Object[] parameterValues) {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(parameterValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object[] stub(StubbingContext context, Constructor<?> constructor) {
        return stream(constructor.getParameters())
                .map(parameter -> stub(context, constructor, parameter))
                .toArray(Object[]::new);
    }

    private Object stub(StubbingContext context, Constructor<?> constructor, Parameter parameter) {
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(context.getSite(), constructor, parameter);
        return context.getStubber().stub(parameter.getParameterizedType(), site);
    }

    private Optional<Constructor<?>> getConstructor(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(this::isInstantiable)
                .flatMap(rawType -> getConstructor(context, rawType));
    }

    private boolean isInstantiable(Class<?> clazz) {
        return !isAbstract(clazz.getModifiers())
                && !clazz.isPrimitive()
                && !clazz.isEnum()
                && !clazz.isInterface();
    }

    private Optional<Constructor<?>> getConstructor(StubbingContext context, Class<?> type) {
        return constructorsByClass.computeIfAbsent(type, clazz -> {
            List<Constructor<?>> constructors = getConstructors(clazz);
            return constructorSelector.select(context, constructors);
        });
    }

    private List<Constructor<?>> getConstructors(Class<?> type) {
        return stream(type.getDeclaredConstructors())
                .filter(constructor -> !constructor.isSynthetic() && !isPrivate(constructor.getModifiers()))
                .collect(toImmutableList());
    }

}