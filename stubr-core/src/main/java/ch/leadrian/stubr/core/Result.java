package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Class representing the result of a stubbing attempt using {@link RootStubber#tryToStub}.
 * Instances of {@link Result} can be either a success or a failure.
 * <p>
 * The behaviour is similar to {@link java.util.Optional} with the key difference that {@code null}
 * is a valid value for success. While mapping success values to other values is supported for convenience,
 * filtering is not, as {@link Result} represents the outcome of a {@link RootStubber#tryToStub} to stub call.
 *
 * @param <T> type of the stub value
 * @see RootStubber#tryToStub
 */
public abstract class Result<T> {

    /**
     * Creates an {@link Result} representing a successful stubbing.
     *
     * @param value stub value returned by a {@link RootStubber#tryToStub} call
     * @param <T>   type of the stubbed {@code value}
     * @return a success instance of {@link Result} containing the stub {@code value}
     * @see RootStubber#tryToStub
     */
    public static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates an {@link Result} representing a failed stubbing.
     *
     * @param <T> type of the stubbed {@code value}
     * @return a failure instance of {@link Result} containing no stub value
     * @see RootStubber#tryToStub
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> failure() {
        return (Failure<T>) Failure.INSTANCE;
    }

    private Result() {
    }

    /**
     * Returns the stub value in case of successful result, else throws a {@code java.util.NoSuchElementException}.
     *
     * @return the stub value in case of successful result
     * @throws java.util.NoSuchElementException in case of a failure result
     */
    public abstract T getValue();

    /**
     * @return {@code true} if the result was successful, else {@code false}
     */
    public final boolean isSuccess() {
        return !isFailure();
    }

    /**
     * @return {@code true} if the result was a failure, else {@code false}
     */
    public abstract boolean isFailure();

    /**
     * Maps the stub value of a successful {@link Result} to to another value. In case of a failure result,
     * the {@code mappingFunction} is not applied.
     * <p>
     * The behaviour is analogous to the behaviour of {@link java.util.Optional#map}
     *
     * @param mappingFunction function applied to the stub value of a successful result
     * @param <U>             the resulting type of the mapping function
     * @return a successful {@link Result} containing a value of type {@link U}, else a failure {@link Result} in case of a failure
     */
    public abstract <U> Result<U> map(Function<T, U> mappingFunction);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String toString();

    private static final class Success<T> extends Result<T> {

        private static final EqualsAndHashCode<Success> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Success.class)
                .compareAndHash(Success::getValue)
                .build();

        private final T value;

        private Success(T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public <U> Result<U> map(Function<T, U> mappingFunction) {
            return Result.success(mappingFunction.apply(value));
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS_AND_HASH_CODE.equals(this, obj);
        }

        @Override
        public int hashCode() {
            return EQUALS_AND_HASH_CODE.hashCode(this);
        }

        @Override
        public String toString() {
            return toStringHelper(this)
                    .add("value", value)
                    .toString();
        }
    }

    private static final class Failure<T> extends Result<T> {

        private static final Failure<Object> INSTANCE = new Failure<>();

        private Failure() {
        }

        @Override
        public T getValue() {
            throw new NoSuchElementException();
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public <U> Result<U> map(Function<T, U> mappingFunction) {
            return Result.failure();
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }
}
