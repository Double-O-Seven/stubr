package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.type.TypeLiteral;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static ch.leadrian.stubr.core.matcher.Matchers.any;
import static java.util.Objects.requireNonNull;

public final class Stubbers {

    private static final List<Stubber> EMPTY_DEFAULT_COLLECTIONS = defaultCollections(0);

    private static final List<Stubber> COMMON_DEFAULT_VALUES = ImmutableList.<Stubber>builder()
            .add(constantValue(new Object()))
            .add(constantValue(""))
            .add(constantValue(CharSequence.class, ""))
            .add(constantValue(Number.class, 0))
            .add(constantValue(BigDecimal.ZERO))
            .add(constantValue(BigInteger.ZERO))
            .add(constantValue(LocalDate.of(1970, 1, 1)))
            .add(constantValue(LocalTime.of(0, 0, 0)))
            .add(constantValue(LocalDateTime.of(1970, 1, 1, 0, 0, 0)))
            .add(constantValue(OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)))
            .add(constantValue(ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)))
            .add(constantValue(Instant.EPOCH))
            .add(constantValue(Locale.GERMANY))
            .add(constantValue(OptionalDouble.empty()))
            .add(constantValue(OptionalInt.empty()))
            .add(constantValue(OptionalLong.empty()))
            .add(constantValue(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")))
            .build();

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

    public static Stubber conditional(Stubber delegate, Matcher<? super Type> typeMatcher) {
        return new ConditionalStubber(delegate, typeMatcher);
    }

    public static List<Stubber> defaultCollections(int size) {
        return ImmutableList.<Stubber>builder()
                .add(collection(Collection.class, ArrayList::new, size))
                .add(collection(List.class, ArrayList::new, size))
                .add(collection(ArrayList.class, ArrayList::new, size))
                .add(collection(Vector.class, Vector::new, size))
                .add(collection(Queue.class, LinkedList::new, size))
                .add(collection(LinkedList.class, LinkedList::new, size))
                .add(collection(Deque.class, ArrayDeque::new, size))
                .add(collection(Set.class, HashSet::new, size))
                .add(collection(HashSet.class, HashSet::new, size))
                .add(collection(LinkedHashSet.class, LinkedHashSet::new, size))
                .add(collection(SortedSet.class, TreeSet::new, size))
                .add(collection(NavigableSet.class, TreeSet::new, size))
                .add(collection(TreeSet.class, TreeSet::new, size))
                .add(collection(ConcurrentLinkedQueue.class, ConcurrentLinkedQueue::new, size))
                .add(collection(ConcurrentLinkedDeque.class, ConcurrentLinkedDeque::new, size))
                .add(collection(ConcurrentSkipListSet.class, ConcurrentSkipListSet::new, size))
                .add(map(Map.class, HashMap::new, size))
                .add(map(HashMap.class, HashMap::new, size))
                .add(map(LinkedHashMap.class, LinkedHashMap::new, size))
                .add(map(Hashtable.class, Hashtable::new, size))
                .add(map(SortedMap.class, TreeMap::new, size))
                .add(map(NavigableMap.class, TreeMap::new, size))
                .add(map(TreeMap.class, TreeMap::new, size))
                .add(map(ConcurrentMap.class, ConcurrentHashMap::new, size))
                .add(map(ConcurrentHashMap.class, ConcurrentHashMap::new, size))
                .add(map(ConcurrentNavigableMap.class, ConcurrentSkipListMap::new, size))
                .add(array(size))
                .build();
    }

    public static List<Stubber> emptyDefaultCollections() {
        return EMPTY_DEFAULT_COLLECTIONS;
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

    public static List<Stubber> commonConstantValues() {
        return COMMON_DEFAULT_VALUES;
    }

    public static Stubber constructor(Matcher<? super Constructor<?>> matcher) {
        return new ConstructorStubber(matcher);
    }

    public static Stubber constructor() {
        return constructor(any());
    }

    public static Stubber defaultConstructor() {
        return constructor(((context, value) -> value.getParameterCount() == 0));
    }

    public static Stubber nonDefaultConstructor() {
        return constructor(((context, value) -> value.getParameterCount() > 0));
    }

    public static Stubber defaultValue() {
        return DefaultValueStubber.INSTANCE;
    }

    public static Stubber enumValue() {
        return EnumValueStubber.INSTANCE;
    }

    public static Stubber factoryMethod(Matcher<? super Method> matcher) {
        return new FactoryMethodStubber(matcher);
    }

    public static Stubber factoryMethod() {
        return factoryMethod(any());
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
        return proxy(true);
    }

    public static Stubber rootStubber() {
        return RootStubberStubber.INSTANCE;
    }

    public static <T> Stubber suppliedValue(Class<T> type, IntFunction<? extends T> valueSupplier) {
        return new SuppliedValueStubber(type, valueSupplier);
    }

    public static <T> Stubber suppliedValue(Class<T> type, Supplier<? extends T> valueSupplier) {
        return suppliedValue(type, sequenceNumber -> valueSupplier.get());
    }

    public static <T> Stubber suppliedValue(TypeLiteral<T> typeLiteral, IntFunction<? extends T> valueSupplier) {
        return new SuppliedValueStubber(typeLiteral.getType(), valueSupplier);
    }

    public static <T> Stubber suppliedValue(TypeLiteral<T> typeLiteral, Supplier<? extends T> valueSupplier) {
        return suppliedValue(typeLiteral, (IntFunction<? extends T>) sequenceNumber -> valueSupplier.get());
    }

    public static List<Stubber> commonSuppliedValues() {
        return ImmutableList.<Stubber>builder()
                .add(suppliedValue(AtomicInteger.class, () -> new AtomicInteger(0)))
                .add(suppliedValue(AtomicBoolean.class, () -> new AtomicBoolean(false)))
                .add(suppliedValue(AtomicLong.class, () -> new AtomicLong(0L)))
                .add(suppliedValue(Date.class, () -> new Date(0)))
                .add(suppliedValue(java.sql.Date.class, () -> new java.sql.Date(0)))
                .build();
    }

    public static <T, U extends T> Stubber implementation(Class<T> targetClass, Class<U> implementationClass) {
        return new ImplementationStubber(targetClass, implementationClass);
    }

    public static <T, U extends T> Stubber implementation(TypeLiteral<T> targetTypeLiteral, TypeLiteral<U> implementationTypeLiteral) {
        return new ImplementationStubber(targetTypeLiteral.getType(), implementationTypeLiteral.getType());
    }

    public static <T, U extends T> Stubber implementation(TypeLiteral<T> targetTypeLiteral, Class<U> implementationClass) {
        return new ImplementationStubber(targetTypeLiteral.getType(), implementationClass);
    }

    public static <T, U extends T> Stubber implementation(Class<T> targetClass, TypeLiteral<U> implementationTypeLiteral) {
        return new ImplementationStubber(targetClass, implementationTypeLiteral.getType());
    }

}
