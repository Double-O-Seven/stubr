package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.ParameterMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.TypeMatcher;
import ch.leadrian.stubr.core.matcher.ConstructorMatchers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Stubbers {

    private Stubbers() {
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, IntSupplier collectionSize) {
        requireNonNull(collectionClass, "collectionClass may not be null");
        requireNonNull(collectionFactory, "collectionFactory may not be null");
        return new CollectionStubber<>(collectionClass, collectionFactory, collectionSize);
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory, IntSupplier collectionSize) {
        requireNonNull(collectionFactory, "collectionFactory may not be null");
        Function<List<Object>, T> actualCollectionFactory = values -> {
            T collection = collectionFactory.get();
            collection.addAll(values);
            return collection;
        };
        return collection(collectionClass, actualCollectionFactory, collectionSize);
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory, int collectionSize) {
        return collection(collectionClass, collectionFactory, () -> collectionSize);
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory) {
        requireNonNull(collectionFactory, "collectionFactory may not be null");
        return collection(collectionClass, values -> collectionFactory.get(), () -> 0);
    }

    public static Stubber conditional(Stubber delegate, TypeMatcher typeMatcher, ParameterMatcher parameterMatcher) {
        return new ConditionalStubber(delegate, typeMatcher, parameterMatcher);
    }

    public static Stubber conditional(Stubber delegate, TypeMatcher typeMatcher) {
        return conditional(delegate, typeMatcher, parameter -> true);
    }

    public static Stubber conditional(Stubber delegate, ParameterMatcher parameterMatcher) {
        return conditional(delegate, type -> true, parameterMatcher);
    }

    public static Stubber constantValue(Object value) {
        requireNonNull(value, "value may not be null");
        return new ConstantValueStubber(value.getClass(), value);
    }

    public static <T> Stubber constantValue(Class<T> targetClass, T value) {
        requireNonNull(targetClass, "targetClass may not be null");
        requireNonNull(value, "value may not be null");
        return new ConstantValueStubber(targetClass, value);
    }

    public static Stubber constructor(ConstructorMatcher matcher) {
        requireNonNull(matcher, "matcher may not be null");
        return new ConstructorStubber(matcher);
    }

    public static Stubber constructor() {
        return constructor(ConstructorMatchers.any());
    }

    public static Stubber defaultValue() {
        return DefaultValueStubber.INSTANCE;
    }

    public static <T extends Map<Object, Object>> Stubber map(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, IntSupplier mapSize) {
        requireNonNull(mapClass, "mapClass may not be null");
        requireNonNull(mapFactory, "mapFactory may not be null");
        return new MapStubber<>(mapClass, mapFactory, mapSize);
    }

    public static <T extends Map<Object, Object>> Stubber map(Class<T> mapClass, Supplier<? extends T> mapFactory, IntSupplier mapSize) {
        requireNonNull(mapFactory, "mapFactory may not be null");
        Function<Map<Object, Object>, T> actualMapFactory = values -> {
            T map = mapFactory.get();
            map.putAll(values);
            return map;
        };
        return map(mapClass, actualMapFactory, mapSize);
    }

    public static <T extends Map<Object, Object>> Stubber map(Class<T> mapClass, Supplier<? extends T> mapFactory, int mapSize) {
        return map(mapClass, mapFactory, () -> mapSize);
    }

    public static <T extends Map<Object, Object>> Stubber map(Class<T> mapClass, Supplier<? extends T> mapFactory) {
        requireNonNull(mapFactory, "mapFactory may not be null");
        return map(mapClass, values -> mapFactory.get(), () -> 0);
    }

    public static Stubber nullValue() {
        return NullValueStubber.INSTANCE;
    }

    public static Stubber object() {
        return ObjectStubber.INSTANCE;
    }

    public static Stubber optional() {
        return OptionalStubber.INSTANCE;
    }

    public static Stubber proxy(ClassLoader classLoader) {
        return new ProxyStubber(classLoader);
    }

    public static Stubber proxy() {
        return proxy(Stubbers.class.getClassLoader());
    }
}
