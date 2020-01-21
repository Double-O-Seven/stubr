package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.stubbingsite.MethodParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

    private final Matcher<? super Method> methodMatcher;
    private final Map<Class<?>, Method> factoryMethodsByClass = new ConcurrentHashMap<>();

    FactoryMethodStubber(Matcher<? super Method> methodMatcher) {
        requireNonNull(methodMatcher, "methodMatcher");
        this.methodMatcher = methodMatcher;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getFactoryMethod(context, type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Method method = getFactoryMethod(context, type)
                .orElseThrow(() -> new StubbingException("No matching factory method found", context.getSite(), type));
        Object[] parameterValues = stub(context, method);
        try {
            return method.invoke(null, parameterValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private Object[] stub(StubbingContext context, Method method) {
        return stream(method.getParameters())
                .map(parameter -> stub(context, method, parameter))
                .toArray(Object[]::new);
    }

    private Object stub(StubbingContext context, Method method, Parameter parameter) {
        MethodParameterStubbingSite site = StubbingSites.methodParameter(context.getSite(), method, parameter);
        return context.getStubber().stub(parameter.getParameterizedType(), site);
    }

    private Optional<Method> getFactoryMethod(StubbingContext context, Type type) {
        return getRawType(type).flatMap(rawType -> getFactoryMethod(context, rawType));
    }

    private Optional<Method> getFactoryMethod(StubbingContext context, Class<?> targetClass) {
        Method method = factoryMethodsByClass.computeIfAbsent(targetClass, c -> {
            List<Method> constructors = getFactoryMethods(context, targetClass);
            if (constructors.size() == 1) {
                return constructors.get(0);
            }
            return null;
        });
        return Optional.ofNullable(method);
    }

    private List<Method> getFactoryMethods(StubbingContext context, Class<?> targetClass) {
        return stream(targetClass.getDeclaredMethods())
                .filter(method -> !method.isSynthetic() && !isPrivate(method.getModifiers()) && isStatic(method.getModifiers()))
                .filter(method -> canReturn(method, targetClass))
                .filter(method -> methodMatcher.matches(context, method))
                .collect(toList());
    }

    private boolean canReturn(Method method, Class<?> type) {
        return getRawType(method.getGenericReturnType())
                .filter(clazz -> clazz.isAssignableFrom(type))
                .isPresent();
    }

}