package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
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

/**
 * Collection of factory methods for various implementations of default {@link StubbingStrategy}s.
 */
public final class StubbingStrategies {

    private static final List<StubbingStrategy> EMPTY_DEFAULT_COLLECTIONS = defaultCollections(0);

    private static final List<StubbingStrategy> COMMON_DEFAULT_VALUES = ImmutableList.<StubbingStrategy>builder()
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

    private StubbingStrategies() {
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub object arrays as well as primitive arrays. A {@link
     * ToIntFunction} must be provided to determine the array size. The array size may be constant, might be derive from
     * annotations present at the stubbing site or it might be anything else.
     *
     * @param arraySize the provided array size
     * @return a {@link StubbingStrategy} for stubbing arrays.
     */
    public static StubbingStrategy array(ToIntFunction<? super StubbingContext> arraySize) {
        return new ArrayStubbingStrategy(arraySize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub object arrays as well as primitive arrays. All arrays stubbed
     * with this strategy will have a constant {@code arraySize}.
     *
     * @param arraySize the array size
     * @return a {@link StubbingStrategy} for stubbing arrays.
     */
    public static StubbingStrategy array(int arraySize) {
        return array(context -> arraySize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub empty object arrays as well as empty primitive arrays.
     *
     * @return a {@link StubbingStrategy} for stubbing empty arrays.
     */
    public static StubbingStrategy array() {
        return array(0);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub collections. A {@link ToIntFunction} must be provided to
     * determine the collection size. The collection size may be constant, might be derive from annotations present at
     * the stubbing site or it might be anything else.
     *
     * @param collectionClass   the collection class that should be stubbed
     * @param collectionFactory a factory providing a concrete instance of the collection class
     * @param collectionSize    the provided collection size
     * @param <T>               the actual type of the collection
     * @return a {@link StubbingStrategy} for stubbing collections.
     */
    public static <T extends Collection> StubbingStrategy collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, ToIntFunction<? super StubbingContext> collectionSize) {
        return new CollectionStubbingStrategy<>(collectionClass, collectionFactory, collectionSize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub collections. All collections stubbed with this strategy will have
     * a constant {@code arraySize}.
     *
     * @param collectionClass   the collection class that should be stubbed
     * @param collectionFactory a factory providing a concrete instance of the collection class
     * @param collectionSize    the collection size
     * @param <T>               the actual type of the collection
     * @return a {@link StubbingStrategy} for stubbing collections.
     */
    public static <T extends Collection> StubbingStrategy collection(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, int collectionSize) {
        return collection(collectionClass, collectionFactory, context -> collectionSize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub empty collections.
     *
     * @param collectionClass   the collection class that should be stubbed
     * @param collectionFactory a factory providing a concrete instance of the collection class
     * @param <T>               the actual type of the collection
     * @return a {@link StubbingStrategy} for stubbing empty collections.
     */
    public static <T extends Collection> StubbingStrategy collection(Class<T> collectionClass, Supplier<? extends T> collectionFactory) {
        requireNonNull(collectionFactory, "collectionFactory");
        return collection(collectionClass, values -> collectionFactory.get(), 0);
    }

    /**
     * Creates a {@link StubbingStrategy} that is only applied both {@code delegate} and the given {@link Matcher}
     * accept a given {@link StubbingContext} and {@link Type}. The created stubbing strategy will delegate to {@code
     * delegate} when stubbing a value.
     *
     * @param delegate    the delegated {@link StubbingStrategy}
     * @param typeMatcher an additional criteria for the returned {@link StubbingStrategy} to accepts a {@link
     *                    StubbingContext} and {@link Type}
     * @return a conditional {@link StubbingStrategy}
     */
    public static StubbingStrategy conditional(StubbingStrategy delegate, Matcher<? super Type> typeMatcher) {
        return new ConditionalStubbingStrategy(delegate, typeMatcher);
    }

    /**
     * Returns a list of {@link StubbingStrategy}s to stub common {@link Collection}s (including {@link Map}s) with size
     * {@code size}.
     *
     * @param size the collection size function
     * @return a list of {@link StubbingStrategy}s for common collections
     */
    public static List<StubbingStrategy> defaultCollections(ToIntFunction<? super StubbingContext> size) {
        return ImmutableList.<StubbingStrategy>builder()
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

    /**
     * Returns a list of {@link StubbingStrategy}s to stub common {@link Collection}s (including {@link Map}s) with size
     * {@code size}.
     *
     * @param size the collection size
     * @return a list of {@link StubbingStrategy}s for common collections
     */
    public static List<StubbingStrategy> defaultCollections(int size) {
        return defaultCollections((ToIntFunction<? super StubbingContext>) context -> size);
    }

    /**
     * Returns a list of {@link StubbingStrategy}s to stub common {@link Collection}s (including {@link Map}s). All
     * stubbed collections will be empty.
     *
     * @return a list of {@link StubbingStrategy}s for common collections
     */
    public static List<StubbingStrategy> emptyDefaultCollections() {
        return EMPTY_DEFAULT_COLLECTIONS;
    }

    /**
     * Returns a {@link StubbingStrategy} using the given value every time the type of the given {@code value} is
     * encountered.
     * <p>
     * Beware that only the {@link StubbingStrategy} only accepts types that are equal to the type of the given {@code
     * value}. Supertypes will not be accepted.
     * <p>
     * The actual class of {@code value} will be used to determine whether the {@code StubbingStrategy} accepts a given
     * type. It is therefore only recommended to not explicitly specify the target type when it is absolutely clear,
     * what type the given {@code value} has.
     *
     * @param value the value to be used as stub value
     * @return a {@link StubbingStrategy} using the given value every time the type of the given {@code value} is
     * encountered
     */
    public static StubbingStrategy constantValue(Object value) {
        return new ConstantValueStubbingStrategy(value.getClass(), value);
    }

    /**
     * Returns a {@link StubbingStrategy} using the given value every time type {@link T} is encountered.
     * <p>
     * Beware that only the {@link StubbingStrategy} only accepts types that are equal to the given type {@link T}.
     * Supertypes will not be accepted.
     *
     * @param targetClass the type that the stubbing strategy will accept.
     * @param value       the value to be used as stub value
     * @param <T>         the actual type of the value
     * @return a {@link StubbingStrategy} using the given value every time type {@link T} is encountered
     */
    public static <T> StubbingStrategy constantValue(Class<T> targetClass, T value) {
        return new ConstantValueStubbingStrategy(targetClass, value);
    }

    /**
     * Returns a {@link StubbingStrategy} using the given value every time type {@link T} is encountered.
     * <p>
     * Beware that only the {@link StubbingStrategy} only accepts types that are equal to the given type {@link T}.
     * Supertypes will not be accepted.
     *
     * @param type  the type that the stubbing strategy will accept.
     * @param value the value to be used as stub value
     * @param <T>   the actual type of the value
     * @return a {@link StubbingStrategy} using the given value every time type {@link T} is encountered
     */
    public static <T> StubbingStrategy constantValue(TypeLiteral<T> type, T value) {
        return new ConstantValueStubbingStrategy(type.getType(), value);
    }

    /**
     * Returns a list of {@link StubbingStrategy}s providing default immutable values for commonly used types.
     * <p>
     * Supported types are the following:
     * <ul>
     * <li>{@link Object}</li>
     * <li>{@link String}</li>
     * <li>{@link CharSequence}</li>
     * <li>{@link Number}</li>
     * <li>{@link BigDecimal}</li>
     * <li>{@link BigInteger}</li>
     * <li>{@link LocalDate}</li>
     * <li>{@link LocalTime}</li>
     * <li>{@link LocalDateTime}</li>
     * <li>{@link OffsetDateTime}</li>
     * <li>{@link ZonedDateTime}</li>
     * <li>{@link Instant}</li>
     * <li>{@link Locale}</li>
     * <li>{@link OptionalDouble}</li>
     * <li>{@link OptionalInt}</li>
     * <li>{@link OptionalLong}</li>
     * <li>{@link UUID}</li>
     * </ul>
     *
     * @return a list of {@link StubbingStrategy}s providing default immutable values for commonly used types
     */
    public static List<StubbingStrategy> commonConstantValues() {
        return COMMON_DEFAULT_VALUES;
    }

    /**
     * Returns a {@link StubbingStrategy} that uses a {@link Constructor} that matches the given {@code matcher} to stub
     * a value.
     * <p>
     * The strategy will only accept a given type, if there is exactly one non-private constructor that matches the
     * given {@code matcher}.
     *
     * @param matcher matcher used to select a suitable constructor
     * @return a {@link StubbingStrategy} that uses a {@link Constructor}
     */
    public static StubbingStrategy constructor(Matcher<? super Constructor<?>> matcher) {
        return new ConstructorStubbingStrategy(matcher);
    }

    /**
     * Returns a {@link StubbingStrategy} that uses a single non-private constructor.
     * <p>
     * The strategy will only accept a given type, if there is exactly one non-private constructor.
     *
     * @return a {@link StubbingStrategy} that uses a {@link Constructor}
     */
    public static StubbingStrategy constructor() {
        return constructor(any());
    }

    /**
     * Returns a {@link StubbingStrategy} that uses the constructor.
     * <p>
     * The strategy will only accept a given type, if there is non-private default constructor.
     *
     * @return a {@link StubbingStrategy} that uses a {@link Constructor}
     */
    public static StubbingStrategy defaultConstructor() {
        return constructor(((context, value) -> value.getParameterCount() == 0));
    }

    /**
     * Returns a {@link StubbingStrategy} that uses a single non-private non-default constructor.
     * <p>
     * The strategy will only accept a given type, if there is non-private non-default constructor.
     *
     * @return a {@link StubbingStrategy} that uses a {@link Constructor}
     */
    public static StubbingStrategy nonDefaultConstructor() {
        return constructor(((context, value) -> value.getParameterCount() > 0));
    }

    /**
     * Returns a stubber that uses the primitive default values for all primitives and their wrappers.
     * <p>
     * Specifically the following types are accepted:
     * <ul>
     * <li>{@code boolean}</li>
     * <li>{@code char}</li>
     * <li>{@code byte}</li>
     * <li>{@code short}</li>
     * <li>{@code int}</li>
     * <li>{@code long}</li>
     * <li>{@code float}</li>
     * <li>{@code double}</li>
     * <li>{@link Boolean}</li>
     * <li>{@link Character}</li>
     * <li>{@link Byte}</li>
     * <li>{@link Short}</li>
     * <li>{@link Integer}</li>
     * <li>{@link Long}</li>
     * <li>{@link Float}</li>
     * <li>{@link Double}</li>
     * </ul>
     *
     * @return a {@link StubbingStrategy} that uses primitive default values
     */
    public static StubbingStrategy defaultValue() {
        return DefaultValueStubbingStrategy.INSTANCE;
    }

    /**
     * Returns a {@link StubbingStrategy} that accepts enum classes and uses the first enum value as stub value.
     * <p>
     * An enum class will not be accepted if no values have been declared.
     *
     * @return a {@link StubbingStrategy} that uses the first enum value of an enum class
     */
    public static StubbingStrategy enumValue() {
        return EnumValueStubbingStrategy.INSTANCE;
    }

    /**
     * Returns a {@link StubbingStrategy} that uses a single factory method that matches the given {@code matcher}.
     * <p>
     * The factory method must satisfy the following criteria:
     * <ul>
     * <li>non-private</li>
     * <li>static</li>
     * <li>returns exactly the type for which a stub is requested</li>
     * <li>declared in the class of the type for which a stub is requested</li>
     * </ul>
     * <p>
     * If multiple factory methods matching the given matcher are present, the strategy will not accept the given type.
     *
     * @param matcher matcher used to select a suitable factory method
     * @return a {@link StubbingStrategy} that uses a single factory method
     */
    public static StubbingStrategy factoryMethod(Matcher<? super Method> matcher) {
        return new FactoryMethodStubbingStrategy(matcher);
    }

    /**
     * Returns a {@link StubbingStrategy} that uses a single factory method.
     * <p>
     * The factory method must satisfy the same criteria as described for {@link StubbingStrategies#factoryMethod(Matcher)}.
     * <p>
     * If multiple factory methods are present, the strategy will not accept the given type.
     *
     * @return a {@link StubbingStrategy} that uses a single factory method
     * @see StubbingStrategies#factoryMethod(Matcher)
     */
    public static StubbingStrategy factoryMethod() {
        return factoryMethod(any());
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub maps. A {@link ToIntFunction} must be provided to determine the
     * map size. The map size may be constant, might be derive from annotations present at the stubbing site or it might
     * be anything else.
     *
     * @param mapClass   the map class that should be stubbed
     * @param mapFactory a factory providing a concrete instance of the map class
     * @param mapSize    the provided map size
     * @param <T>        the actual type of the map
     * @return a {@link StubbingStrategy} for stubbing maps.
     */
    public static <T extends Map> StubbingStrategy map(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, ToIntFunction<? super StubbingContext> mapSize) {
        return new MapStubbingStrategy<>(mapClass, mapFactory, mapSize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub maps. All maps stubbed with this strategy will have a constant
     * {@code arraySize}.
     *
     * @param mapClass   the map class that should be stubbed
     * @param mapFactory a factory providing a concrete instance of the map class
     * @param mapSize    the map size
     * @param <T>        the actual type of the map
     * @return a {@link StubbingStrategy} for stubbing maps.
     */
    public static <T extends Map> StubbingStrategy map(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, int mapSize) {
        return map(mapClass, mapFactory, context -> mapSize);
    }

    /**
     * Creates a {@link StubbingStrategy} used to stub empty maps.
     *
     * @param mapClass   the map class that should be stubbed
     * @param mapFactory a factory providing a concrete instance of the map class
     * @param <T>        the actual type of the map
     * @return a {@link StubbingStrategy} for stubbing empty maps.
     */
    public static <T extends Map> StubbingStrategy map(Class<T> mapClass, Supplier<? extends T> mapFactory) {
        requireNonNull(mapFactory, "mapFactory");
        return map(mapClass, values -> mapFactory.get(), 0);
    }

    /**
     * Creates a {@link StubbingStrategy} that memoizes the computed stub for a given type.
     * <p>
     * Beware though: Only equal types will return the stub value. {@code List&lt;? extends CharSequence&gt;} and {@code
     * List&lt;CharSequence&gt;} will <i>not</i> return the same stub value.
     *
     * @param delegate the wrapped stubbing strategy
     * @return a {@link StubbingStrategy} that memoizes the computed stub for a given type
     */
    public static StubbingStrategy memoizing(StubbingStrategy delegate) {
        return new MemoizingStubbingStrategy(delegate);
    }

    /**
     * Returns a {@link StubbingStrategy} that always uses {@code null} to provide stubs.
     *
     * @return a {@link StubbingStrategy} that always uses {@code null} to provide stubs
     */
    public static StubbingStrategy nullValue() {
        return NullValueStubbingStrategy.INSTANCE;
    }

    /**
     * Returns a {@link StubbingStrategy} that stubs {@link java.util.Optional}s.
     * <p>
     * Depending on the {@link OptionalStubbingMode} {@code code} values will either be always empty, present or present
     * if a stub value for the wrapped type can be provided.
     *
     * @param mode the mode defining how values will be stubbed.
     * @return a {@link StubbingStrategy} that stubs {@link java.util.Optional}s
     * @see OptionalStubbingMode
     */
    public static StubbingStrategy optional(OptionalStubbingMode mode) {
        return mode.getStrategy();
    }

    /**
     * Returns a {@link StubbingStrategy} always providing {@link java.util.Optional#empty()}.
     * <p>
     * The behaviour is the same as using {@link StubbingStrategies#optional(OptionalStubbingMode)} with mode {@link
     * OptionalStubbingMode#EMPTY}.
     *
     * @return a {@link StubbingStrategy} always providing {@link java.util.Optional#empty()}
     */
    public static StubbingStrategy optional() {
        return optional(OptionalStubbingMode.EMPTY);
    }

    /**
     * Returns a {@link StubbingStrategy} that creates stub values for interfaces using {@link
     * java.lang.reflect.Proxy#newProxyInstance(ClassLoader, Class[], java.lang.reflect.InvocationHandler)}. Any
     * non-void method return a stub value provided by the {@link ch.leadrian.stubr.core.Stubber} of the context used to
     * the created the proxy instance.
     * <p>
     * If {@code cacheStubs} is set to {@code true}, the returned stub values will be cached. If it is set to {@code
     * false}, a new stub value will be computed every time a method will be invoked.
     * <p>
     * If a method has a default implementation, the real method will be invoked.
     *
     * @param cacheStubs flag that determines whether method return values will be cached or not
     * @return a {@link StubbingStrategy} that creates stub values for interfaces using proxies
     */
    public static StubbingStrategy proxy(boolean cacheStubs) {
        return cacheStubs ? ProxyStubbingStrategy.CACHING : ProxyStubbingStrategy.NON_CACHING;
    }

    /**
     * Returns a {@link StubbingStrategy} that creates stub values for interfaces using {@link
     * java.lang.reflect.Proxy#newProxyInstance(ClassLoader, Class[], java.lang.reflect.InvocationHandler)}. Any method
     * return values will be cached.
     * <p>
     * The behaviour is the same as using {@link StubbingStrategies#proxy(boolean)} when {@code true} is passed.
     *
     * @return a {@link StubbingStrategy} that creates stub values for interfaces using proxies
     * @see StubbingStrategies#proxy(boolean)
     */
    public static StubbingStrategy proxy() {
        return proxy(true);
    }

    /**
     * Returns a {@link StubbingStrategy} providing the {@link ch.leadrian.stubr.core.Stubber} given by {@link
     * StubbingContext#getStubber()} passed to {@link StubbingStrategy#stub(StubbingContext, Type)}.
     *
     * @return a {@link StubbingStrategy} providing the {@link ch.leadrian.stubr.core.Stubber}
     */
    public static StubbingStrategy stubber() {
        return StubberStubbingStrategy.INSTANCE;
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}. The passed {@link StubValueSupplier} is invoked with a {@link StubbingContext} and a sequence
     * number that will be atomically increased on every call.
     *
     * @param type          the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(Class<T> type, StubValueSupplier<? extends T> valueSupplier) {
        return new SuppliedValueStubbingStrategy(type, valueSupplier);
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}. The passed {@link IntFunction} is invoked with a sequence number that will be atomically
     * increased on every call.
     *
     * @param type          the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(Class<T> type, IntFunction<? extends T> valueSupplier) {
        requireNonNull(valueSupplier, "valueSupplier");
        return suppliedValue(type, (context, sequenceNumber) -> valueSupplier.apply(sequenceNumber));
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}.
     *
     * @param type          the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(Class<T> type, Supplier<? extends T> valueSupplier) {
        requireNonNull(valueSupplier, "valueSupplier");
        return suppliedValue(type, sequenceNumber -> valueSupplier.get());
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}. The passed {@link StubValueSupplier} is invoked with a {@link StubbingContext} and a sequence
     * number that will be atomically increased on every call.
     *
     * @param typeLiteral   the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(TypeLiteral<T> typeLiteral, StubValueSupplier<? extends T> valueSupplier) {
        return new SuppliedValueStubbingStrategy(typeLiteral.getType(), valueSupplier);
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}. The passed {@link IntFunction} is invoked with a sequence number that will be atomically
     * increased on every call.
     *
     * @param typeLiteral   the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(TypeLiteral<T> typeLiteral, IntFunction<? extends T> valueSupplier) {
        requireNonNull(valueSupplier, "valueSupplier");
        return suppliedValue(typeLiteral, ((context, sequenceNumber) -> valueSupplier.apply(sequenceNumber)));
    }

    /**
     * Returns a {@link StubbingStrategy} providing a stub value for the given {@link Class} using the {@code
     * valueSupplier}.
     *
     * @param typeLiteral   the type for which a stub value should be supplied
     * @param valueSupplier the value supplying function
     * @param <T>           the generic type
     * @return a {@link StubbingStrategy} providing a stub values using a supplying function
     */
    public static <T> StubbingStrategy suppliedValue(TypeLiteral<T> typeLiteral, Supplier<? extends T> valueSupplier) {
        requireNonNull(valueSupplier, "valueSupplier");
        return suppliedValue(typeLiteral, sequenceNumber -> valueSupplier.get());
    }

    /**
     * Returns a list of {@link StubbingStrategy}s for commonly used mutable objects.
     * <p>
     * Supported types are:
     * <ul>
     * <li>{@link AtomicInteger}</li>
     * <li>{@link AtomicBoolean}</li>
     * <li>{@link AtomicLong}</li>
     * <li>{@link Date}</li>
     * <li>{@link java.sql.Date}</li>
     * </ul>
     *
     * @return a list of {@link StubbingStrategy}s for commonly used mutable objects
     */
    public static List<StubbingStrategy> commonSuppliedValues() {
        return ImmutableList.<StubbingStrategy>builder()
                .add(suppliedValue(AtomicInteger.class, () -> new AtomicInteger(0)))
                .add(suppliedValue(AtomicBoolean.class, () -> new AtomicBoolean(false)))
                .add(suppliedValue(AtomicLong.class, () -> new AtomicLong(0L)))
                .add(suppliedValue(Date.class, () -> new Date(0)))
                .add(suppliedValue(java.sql.Date.class, () -> new java.sql.Date(0)))
                .build();
    }

    /**
     * Returns a {@link StubbingStrategy} that provides a stub value for the given target type {@link T} by looking up a
     * stub value for the given implementation type {@link U}.
     * <p>
     * The actual stub value provided by getting a stub value for {@link U} from the {@link
     * ch.leadrian.stubr.core.Stubber} given by {@link StubbingContext#getStubber()}.
     *
     * @param targetClass         the type to be stubbed
     * @param implementationClass the actual type of the stub value
     * @param <T>                 the target type
     * @param <U>                 the implementation type
     * @return a {@link StubbingStrategy} that delegates the stubbing of an instance of {@link T} to stubbing a value
     * for {@link U}
     */
    public static <T, U extends T> StubbingStrategy implementation(Class<T> targetClass, Class<U> implementationClass) {
        return new ImplementationStubbingStrategy(targetClass, implementationClass);
    }

    /**
     * Returns a {@link StubbingStrategy} that provides a stub value for the given target type {@link T} by looking up a
     * stub value for the given implementation type {@link U}.
     * <p>
     * The actual stub value provided by getting a stub value for {@link U} from the {@link
     * ch.leadrian.stubr.core.Stubber} given by {@link StubbingContext#getStubber()}.
     *
     * @param targetTypeLiteral         the type to be stubbed
     * @param implementationTypeLiteral the actual type of the stub value
     * @param <T>                       the target type
     * @param <U>                       the implementation type
     * @return a {@link StubbingStrategy} that delegates the stubbing of an instance of {@link T} to stubbing a value
     * for {@link U}
     */
    public static <T, U extends T> StubbingStrategy implementation(TypeLiteral<T> targetTypeLiteral, TypeLiteral<U> implementationTypeLiteral) {
        return new ImplementationStubbingStrategy(targetTypeLiteral.getType(), implementationTypeLiteral.getType());
    }

    /**
     * Returns a {@link StubbingStrategy} that provides a stub value for the given target type {@link T} by looking up a
     * stub value for the given implementation type {@link U}.
     * <p>
     * The actual stub value provided by getting a stub value for {@link U} from the {@link
     * ch.leadrian.stubr.core.Stubber} given by {@link StubbingContext#getStubber()}.
     *
     * @param targetTypeLiteral   the type to be stubbed
     * @param implementationClass the actual type of the stub value
     * @param <T>                 the target type
     * @param <U>                 the implementation type
     * @return a {@link StubbingStrategy} that delegates the stubbing of an instance of {@link T} to stubbing a value
     * for {@link U}
     */
    public static <T, U extends T> StubbingStrategy implementation(TypeLiteral<T> targetTypeLiteral, Class<U> implementationClass) {
        return new ImplementationStubbingStrategy(targetTypeLiteral.getType(), implementationClass);
    }

    /**
     * Returns a {@link StubbingStrategy} that provides a stub value for the given target type {@link T} by looking up a
     * stub value for the given implementation type {@link U}.
     * <p>
     * The actual stub value provided by getting a stub value for {@link U} from the {@link
     * ch.leadrian.stubr.core.Stubber} given by {@link StubbingContext#getStubber()}.
     *
     * @param targetClass               the type to be stubbed
     * @param implementationTypeLiteral the actual type of the stub value
     * @param <T>                       the target type
     * @param <U>                       the implementation type
     * @return a {@link StubbingStrategy} that delegates the stubbing of an instance of {@link T} to stubbing a value
     * for {@link U}
     */
    public static <T, U extends T> StubbingStrategy implementation(Class<T> targetClass, TypeLiteral<U> implementationTypeLiteral) {
        return new ImplementationStubbingStrategy(targetClass, implementationTypeLiteral.getType());
    }

}
