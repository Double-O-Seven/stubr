package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import com.google.common.primitives.Primitives;

import java.lang.reflect.Type;
import java.util.List;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static com.google.common.primitives.Primitives.wrap;
import static java.util.Arrays.asList;

/**
 * Class {@code RootStubber} represents the main API used to stub a specific type. A {@code RootStubber} may be a
 * composition of several other {@code RootStubber}s or it may be a composition of several {@link Stubber}s and
 * optionally other {@code RootStubber}s
 *
 * @see Stubber
 */
public abstract class RootStubber {

    /**
     * Creates a new {@link RootStubberBuilder} that can be used to configure a {@code RootStubber} instance.
     *
     * @return a new {@link RootStubberBuilder}
     * @see RootStubberBuilder
     */
    public static RootStubberBuilder builder() {
        return new RootStubberImpl.Builder();
    }

    /**
     * Creates a new composite {@code RootStubber} composed from multiple {@code RootStubber}s. The resulting {@code
     * RootStubber} will iterate through the given {@code rootStubbers} in list order and return the stub value of the
     * first successful {@link Result} returned by a {@code RootStubber}. If no {@code RootStubber} returns a successful
     * result, a failure {@link Result} will be returned.
     *
     * @return the first successful {@link Result} of the given {@code rootStubbers}, or a failure {@link Result}
     * @see Result
     */
    public static RootStubber compose(List<? extends RootStubber> rootStubbers) {
        return new CompositeRootStubber(rootStubbers);
    }

    /**
     * The behaviour is the same as for {@link RootStubber#compose(List)}.
     *
     * @return the first successful {@link Result} of the given {@code rootStubbers}, or a failure {@link Result}
     * @see Result
     * @see RootStubber#compose(List)
     */
    public static RootStubber compose(RootStubber... rootStubbers) {
        return compose(asList(rootStubbers));
    }

    /**
     * Actual implementation of the stubbing process. The implementation must guarantee that the value returned by a
     * successful {@link Result} is compatible with the given {@code type}. If no suitable instance can be stubbed, a
     * failure {@link Result} provided by {@link Result#failure()} should be returned.
     * <p>
     * The implementation may replace the given {@link StubbingContext} {@code context} by replacing the encapsulated
     * {@link RootStubber} which an enhanced {@link RootStubber}. However, it must not replace the encapsulated {@link
     * StubbingSite}.
     * <p>
     * This method is not intended to be used directly by any API consumer, it is only meant to provide concrete
     * implementations of the stubbing process to be used in the public API methods of {@code RootStubber}.
     *
     * @param type    the type which should be stubbed
     * @param context {@link StubbingContext} in which a value of type {@code type} should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     */
    protected abstract Result<?> tryToStub(Type type, StubbingContext context);

    public final Result<?> tryToStub(Type type, StubbingSite site) {
        StubbingContext context = new StubbingContext(this, site);
        return tryToStub(type, context);
    }

    public final Result<?> tryToStub(Type type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    public final Object stub(Type type, StubbingSite site) {
        Result<?> result = tryToStub(type, site);
        if (result.isFailure()) {
            throw new StubbingException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    public final Object stub(Type type) {
        return stub(type, StubbingSites.unknown());
    }

    public final <T> Result<T> tryToStub(Class<T> type, StubbingSite site) {
        return tryToStub((Type) type, site).map(value -> wrap(type).cast(value));
    }

    public final <T> Result<T> tryToStub(Class<T> type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    public final <T> T stub(Class<T> type, StubbingSite site) {
        return wrap(type).cast(stub((Type) type, site));
    }

    public final <T> T stub(Class<T> type) {
        return stub(type, StubbingSites.unknown());
    }

    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getWrappedRawType(typeLiteral);
        return tryToStub(typeLiteral.getType(), site).map(rawType::cast);
    }

    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral) {
        return tryToStub(typeLiteral, StubbingSites.unknown());
    }

    public final <T> T stub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getWrappedRawType(typeLiteral);
        return rawType.cast(stub(typeLiteral.getType(), site));
    }

    public final <T> T stub(TypeLiteral<T> typeLiteral) {
        return stub(typeLiteral, StubbingSites.unknown());
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getWrappedRawType(TypeLiteral<T> typeLiteral) {
        Type type = typeLiteral.getType();
        return (Class<T>) getRawType(type)
                .map(Primitives::wrap)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot get raw type of %s", type)));
    }

}
