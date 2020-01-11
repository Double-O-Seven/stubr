package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;
import static java.lang.reflect.Modifier.isPrivate;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

final class ConstructorStubber implements Stubber {

    private final ConstructorMatcher constructorMatcher;
    private final Map<Class<?>, Constructor<?>> constructorsByClass = new ConcurrentHashMap<>();

    ConstructorStubber(ConstructorMatcher constructorMatcher) {
        this.constructorMatcher = constructorMatcher;
    }

    @Override
    public boolean accepts(Type type) {
        return getConstructor(type).isPresent();
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        Constructor<?> constructor = getConstructor(type).orElseThrow(IllegalStateException::new);
        Object[] parameterValues = stream(constructor.getGenericParameterTypes())
                .map(rootStubber::stub)
                .toArray(Object[]::new);
        try {
            return constructor.newInstance(parameterValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Optional<Constructor<?>> getConstructor(Type type) {
        Optional<Class<?>> targetClass = accept(type, new TypeVisitor<Optional<Class<?>>>() {

            @Override
            public Optional<Class<?>> visit(Class<?> clazz) {
                return Optional.of(clazz);
            }

            @Override
            public Optional<Class<?>> visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getRawType(), this);
            }

            @Override
            public Optional<Class<?>> visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType)
                        .map(upperBound -> accept(upperBound, this))
                        .orElseThrow(IllegalStateException::new);
            }

            @Override
            public Optional<Class<?>> visit(TypeVariable<?> typeVariable) {
                return Optional.empty();
            }
        });
        return targetClass.flatMap(this::getConstructor);
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