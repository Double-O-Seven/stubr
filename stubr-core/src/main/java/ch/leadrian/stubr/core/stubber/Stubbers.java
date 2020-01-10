package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ParameterMatcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.TypeMatcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class Stubbers {

    private Stubbers() {
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, int collectionSize) {
        requireNonNull(collectionClass, "collectionClass may not be null");
        requireNonNull(collectionClass, "collectionFactory may not be null");
        return new CollectionStubber<>(collectionClass, collectionFactory, collectionSize);
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory, int collectionSize) {
        requireNonNull(collectionClass, "collectionClass may not be null");
        requireNonNull(collectionClass, "collectionFactory may not be null");
        Function<List<Object>, T> actualCollectionFactory = values -> {
            T collection = collectionFactory.get();
            collection.addAll(values);
            return collection;
        };
        return new CollectionStubber<>(collectionClass, actualCollectionFactory, collectionSize);
    }

    public static <T extends Collection<Object>> Stubber collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory) {
        requireNonNull(collectionClass, "collectionClass may not be null");
        requireNonNull(collectionClass, "collectionFactory may not be null");
        return collection(collectionClass, values -> collectionFactory.get(), 0);
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

    public static Stubber defaultValue() {
        return DefaultValueStubber.INSTANCE;
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
}
