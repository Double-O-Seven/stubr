package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ConstructorMatcher;
import ch.leadrian.stubr.core.MethodMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.TypeMatcher;
import ch.leadrian.stubr.core.matcher.ConstructorMatchers;
import ch.leadrian.stubr.core.matcher.MethodMatchers;
import ch.leadrian.stubr.core.type.TypeLiteral;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static java.util.Objects.requireNonNull;

public final class Stubbers {

    private Stubbers() {
    }

    public static Stubber array(ToIntFunction<? super StubbingContext> arraySize) {
        return new ArrayStubber(arraySize);
    }

    public static Stubber array(int arraySize) {
        return array(context -> arraySize);
    }

    public static Stubber array() {
        return array(0);
    }

    public static <T extends Collection> Stubber collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, ToIntFunction<? super StubbingContext> collectionSize) {
        return new CollectionStubber<>(collectionClass, collectionFactory, collectionSize);
    }

    public static <T extends Collection> Stubber collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, int collectionSize) {
        return collection(collectionClass, collectionFactory, context -> collectionSize);
    }

    public static <T extends Collection> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory) {
        requireNonNull(collectionFactory, "collectionFactory");
        return collection(collectionClass, values -> collectionFactory.get(), 0);
    }

    public static Stubber conditional(Stubber delegate, TypeMatcher typeMatcher) {
        return new ConditionalStubber(delegate, typeMatcher);
    }

    public static Stubber constantValue(Object value) {
        return new ConstantValueStubber(value.getClass(), value);
    }

    public static <T> Stubber constantValue(Class<T> targetClass, T value) {
        return new ConstantValueStubber(targetClass, value);
    }

    public static <T> Stubber constantValue(TypeLiteral<T> type, T value) {
        return new ConstantValueStubber(type.getType(), value);
    }

    public static Stubber constructor(ConstructorMatcher matcher) {
        return new ConstructorStubber(matcher);
    }

    public static Stubber constructor() {
        return constructor(ConstructorMatchers.any());
    }

    public static Stubber defaultValue() {
        return DefaultValueStubber.INSTANCE;
    }

    public static Stubber enumValue() {
        return EnumValueStubber.INSTANCE;
    }

    public static Stubber factoryMethod(MethodMatcher matcher) {
        return new FactoryMethodStubber(matcher);
    }

    public static Stubber factoryMethod() {
        return factoryMethod(MethodMatchers.any());
    }

    public static <T extends Map> Stubber map(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, ToIntFunction<? super StubbingContext> mapSize) {
        return new MapStubber<>(mapClass, mapFactory, mapSize);
    }

    public static <T extends Map> Stubber map(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, int mapSize) {
        return map(mapClass, mapFactory, context -> mapSize);
    }

    public static <T extends Map> Stubber map(Class<T> mapClass, Supplier<? extends T> mapFactory) {
        requireNonNull(mapFactory, "mapFactory");
        return map(mapClass, values -> mapFactory.get(), 0);
    }

    public static Stubber nullValue() {
        return NullValueStubber.INSTANCE;
    }

    public static Stubber optional(OptionalStubbingMode mode) {
        return mode.getStubber();
    }

    public static Stubber optional() {
        return optional(OptionalStubbingMode.EMPTY);
    }

    public static Stubber proxy(boolean cacheStubs) {
        return cacheStubs ? ProxyStubber.CACHING : ProxyStubber.NON_CACHING;
    }

    public static Stubber proxy() {
        return proxy(false);
    }

    public static Stubber rootStubber() {
        return RootStubberStubber.INSTANCE;
    }
}
