/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import ch.leadrian.stubr.core.type.Types;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.common.primitives.Primitives.wrap;
import static java.util.Arrays.asList;

/**
 * Class {@code Stubber} represents the main API used to stub a specific type. A {@code Stubber} may be a composition of
 * several other {@code Stubber}s or it may be a composition of several {@link StubbingStrategy}s and optionally other
 * {@code Stubber}s
 *
 * @see StubbingStrategy
 */
public abstract class Stubber {

    /**
     * Creates a new {@link StubberBuilder} that can be used to configure a {@code Stubber} instance.
     *
     * @return a new {@link StubberBuilder}
     * @see StubberBuilder
     */
    public static StubberBuilder builder() {
        return new StubberImpl.Builder();
    }

    /**
     * Creates a new composite {@code Stubber} composed from multiple {@code Stubber}s. The resulting {@code Stubber}
     * will iterate through the given {@code stubbers} in list order and return the stub value of the first successful
     * {@link Result} returned by a {@code Stubber}. If no {@code Stubber} returns a successful result, a failure {@link
     * Result} will be returned.
     *
     * @param stubbers {@code Stubber}s to be composed into a single {@code Stubber}
     * @return the first successful {@link Result} of the given {@code stubbers}, or a failure {@link Result}
     * @see Result
     */
    public static Stubber compose(List<? extends Stubber> stubbers) {
        return new CompositeStubber(stubbers);
    }

    /**
     * The behaviour is the same as for {@link Stubber#compose(List)}.
     *
     * @param stubbers {@code Stubber}s to be composed into a single {@code Stubber}
     * @return the first successful {@link Result} of the given {@code stubbers}, or a failure {@link Result}
     * @see Result
     * @see Stubber#compose(List)
     */
    public static Stubber compose(Stubber... stubbers) {
        return compose(asList(stubbers));
    }

    /**
     * Actual implementation of the stubbing process. The implementation must guarantee that the value returned by a
     * successful {@link Result} is compatible with the given {@code type}. If no suitable instance can be stubbed, a
     * failure {@link Result} provided by {@link Result#failure()} should be returned.
     * <p>
     * The implementation may replace the given {@link StubbingContext} {@code context} by replacing the encapsulated
     * {@link Stubber} which an enhanced {@link Stubber}. However, it must not replace the encapsulated {@link
     * StubbingSite}.
     * <p>
     * This method is not intended to be used directly by any API consumer, it is only meant to provide concrete
     * implementations of the stubbing process to be used in the public API methods of {@code Stubber}.
     *
     * @param type    the type which should be stubbed
     * @param context {@link StubbingContext} in which a value of type {@code type} should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     */
    protected abstract Result<?> tryToStub(Type type, StubbingContext context);

    /**
     * Public type-unsafe wrapper for {@link Stubber#tryToStub(Type, StubbingContext)}.
     * <p>
     * This method should not be used by the end user of a {@code Stubber} and is only meant to be used by
     * implementations of {@link StubbingStrategy} where the type is not directly known and would require additional
     * instance checking and casting to a {@code Class} for example.
     * <p>
     * Since any implementation of {@link StubbingStrategy} is supposed pass down or wrap the given {@link
     * StubbingSite}, a {@link StubbingSite} must always be provided.
     *
     * @param type the type which should be stubbed
     * @param site {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     * @see Stubber#tryToStub(Type, StubbingContext)
     */
    public final Result<?> tryToStub(Type type, StubbingSite site) {
        StubbingContext context = StubbingContext.create(this, site);
        return tryToStub(type, context);
    }

    /**
     * Public type-unsafe wrapper for {@link Stubber#tryToStub(Type, StubbingSite)}.
     * <p>
     * If {@link Stubber#tryToStub(Type, StubbingContext)} returns a failure {@link Result}, a {@link StubbingException}
     * is thrown.
     * <p>
     * This method should not be used by the end user of a {@code Stubber} and is only meant to be used by
     * implementations of {@link StubbingStrategy} where the type is not directly known and would require additional
     * instance checking and casting to a {@code Class} for example.
     * <p>
     * Since any implementation of {@link StubbingStrategy} is supposed pass down or wrap the given {@link
     * StubbingSite}, a {@link StubbingSite} must always be provided.
     *
     * @param type the type which should be stubbed
     * @param site {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @return a stub value
     * @throws StubbingException if no stub value for the given {@code type} could be provided
     * @see Stubber#tryToStub(Type, StubbingSite)
     */
    public final Object stub(Type type, StubbingSite site) {
        Result<?> result = tryToStub(type, site);
        if (result.isFailure()) {
            throw new StubbingException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    /**
     * Public type-safe wrapper for {@link Stubber#tryToStub(Type, StubbingContext)}.
     *
     * @param type the class representing the generic type {@link T}
     * @param site {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @param <T>  the type which should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     * @see Stubber#tryToStub(Type, StubbingContext)
     */
    public final <T> Result<T> tryToStub(Class<T> type, StubbingSite site) {
        return tryToStub((Type) type, site).map(value -> wrap(type).cast(value));
    }

    /**
     * Public type-safe wrapper for {@link Stubber#tryToStub(Type, StubbingSite)}.
     * <p>
     * Since no {@link StubbingSite} is provided, {@link StubbingSites#unknown()} will be used as site.
     *
     * @param type the class representing the generic type {@link T}
     * @param <T>  the type which should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     * @see Stubber#tryToStub(Type, StubbingSite)
     */
    public final <T> Result<T> tryToStub(Class<T> type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    /**
     * Public type-safe wrapper for {@link Stubber#stub(Type, StubbingSite)}.
     *
     * @param type the class representing the generic type {@link T}
     * @param site {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @param <T>  the type which should be stubbed
     * @return a stub value
     * @throws StubbingException if no stub value for the given {@code type} could be provided
     * @see Stubber#stub(Type, StubbingSite)
     */
    public final <T> T stub(Class<T> type, StubbingSite site) {
        return wrap(type).cast(stub((Type) type, site));
    }

    /**
     * Public type-safe wrapper for {@link Stubber#stub(Class, StubbingSite)}.
     * <p>
     * Since no {@link StubbingSite} is provided, {@link StubbingSites#unknown()} will be used as site.
     *
     * @param type the class representing the generic type {@link T}
     * @param <T>  the type which should be stubbed
     * @return a stub value
     * @throws StubbingException if no stub value for the given {@code type} could be provided
     * @see Stubber#stub(Type, StubbingSite)
     */
    public final <T> T stub(Class<T> type) {
        return stub(type, StubbingSites.unknown());
    }

    /**
     * Public type-safe wrapper for {@link Stubber#tryToStub(Type, StubbingContext)}.
     *
     * @param typeLiteral the {@link TypeLiteral} representing the generic type {@link T}
     * @param site        {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @param <T>         the type which should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     * @see Stubber#tryToStub(Type, StubbingContext)
     * @see TypeLiteral
     */
    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getRawType(typeLiteral);
        return tryToStub(typeLiteral.getType(), site).map(rawType::cast);
    }

    /**
     * Public type-safe wrapper for {@link Stubber#tryToStub(Type, StubbingContext)}.
     * <p>
     * Since no {@link StubbingSite} is provided, {@link StubbingSites#unknown()} will be used as site.
     *
     * @param typeLiteral the {@link TypeLiteral} representing the generic type {@link T}
     * @param <T>         the type which should be stubbed
     * @return a successful {@link Result} containing the stub value, or a failure result
     * @see Stubber#tryToStub(Type, StubbingContext)
     * @see TypeLiteral
     */
    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral) {
        return tryToStub(typeLiteral, StubbingSites.unknown());
    }

    /**
     * Public type-safe wrapper for {@link Stubber#stub(Type, StubbingSite)}.
     *
     * @param typeLiteral the {@link TypeLiteral} representing the generic type {@link T}
     * @param site        {@link StubbingSite} at which a value of type {@code type} should be stubbed
     * @param <T>         the type which should be stubbed
     * @return a stub value
     * @throws StubbingException if no stub value for the given {@code type} could be provided
     * @see Stubber#stub(Type, StubbingSite)
     * @see TypeLiteral
     */
    public final <T> T stub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getRawType(typeLiteral);
        return rawType.cast(stub(typeLiteral.getType(), site));
    }

    /**
     * Public type-safe wrapper for {@link Stubber#stub(Type, StubbingSite)}.
     * <p>
     * Since no {@link StubbingSite} is provided, {@link StubbingSites#unknown()} will be used as site.
     *
     * @param typeLiteral the {@link TypeLiteral} representing the generic type {@link T}
     * @param <T>         the type which should be stubbed
     * @return a stub value
     * @throws StubbingException if no stub value for the given {@code type} could be provided
     * @see Stubber#stub(Type, StubbingSite)
     * @see TypeLiteral
     */
    public final <T> T stub(TypeLiteral<T> typeLiteral) {
        return stub(typeLiteral, StubbingSites.unknown());
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getRawType(TypeLiteral<T> typeLiteral) {
        Type type = typeLiteral.getType();
        return (Class<T>) Types.getRawType(type)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot get raw type of %s", type)));
    }

}
