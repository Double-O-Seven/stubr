package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.ConstructorParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.util.Types.getRawType;
import static java.lang.reflect.Modifier.isPrivate;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class ConstructorStubber implements Stubber {

    private final ConstructorMatcher constructorMatcher;
    private final Map<Class<?>, Constructor<?>> constructorsByClass = new ConcurrentHashMap<>();

    ConstructorStubber(ConstructorMatcher constructorMatcher) {
        requireNonNull(constructorMatcher, "constructorMatcher");
        this.constructorMatcher = constructorMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getConstructor(type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Constructor<?> constructor = getConstructor(type).orElseThrow(IllegalStateException::new);
        Object[] parameterValues = stream(constructor.getParameters())
                .map(parameter -> {
                    ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(context.getSite(), constructor, parameter);
                    return context.getStubber().stub(parameter.getParameterizedType(), site);
                })
                .toArray(Object[]::new);
        try {
            return constructor.newInstance(parameterValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Optional<Constructor<?>> getConstructor(Type type) {
        return getRawType(type).flatMap(this::getConstructor);
    }

    private Optional<Constructor<?>> getConstructor(Class<?> targetClass) {
        Constructor<?> constructor = constructorsByClass.computeIfAbsent(targetClass, clazz -> {
            List<Constructor<?>> constructors = stream(clazz.getConstructors())
                    .filter(c -> !c.isSynthetic() && !isPrivate(c.getModifiers()))
                    .filter(constructorMatcher::matches)
                    .collect(toList());
            if (constructors.size() == 1) {
                return constructors.get(0);
            }
            return null;
        });
        return Optional.ofNullable(constructor);
    }

}