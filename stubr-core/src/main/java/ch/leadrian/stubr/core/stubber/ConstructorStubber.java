package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.ConstructorParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.type.Types.getRawType;
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
        Constructor<?> constructor = getConstructor(type).orElseThrow(UnsupportedOperationException::new);
        Object[] parameterValues = stub(context, constructor);
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

    private Optional<Constructor<?>> getConstructor(Type type) {
        return getRawType(type).flatMap(this::getConstructor);
    }

    private Optional<Constructor<?>> getConstructor(Class<?> type) {
        Constructor<?> constructor = constructorsByClass.computeIfAbsent(type, clazz -> {
            List<Constructor<?>> constructors = getConstructors(clazz);
            if (constructors.size() == 1) {
                return constructors.get(0);
            }
            return null;
        });
        return Optional.ofNullable(constructor);
    }

    private List<Constructor<?>> getConstructors(Class<?> type) {
        return stream(type.getConstructors())
                .filter(c -> !c.isSynthetic() && !isPrivate(c.getModifiers()))
                .filter(constructorMatcher::matches)
                .collect(toList());
    }

}