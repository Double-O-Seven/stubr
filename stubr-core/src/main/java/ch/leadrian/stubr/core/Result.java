package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;

public abstract class Result<T> {

    public static <T> Success<T> success(T value) {
        return new Success<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Failure<T> failure() {
        return (Failure<T>) Failure.INSTANCE;
    }

    private Result() {
    }

    public abstract T getValue();

    public boolean isSuccess() {
        return !isFailure();
    }

    public abstract boolean isFailure();

    public abstract <U> Result<U> map(Function<T, U> mappingFunction);

    public static final class Success<T> extends Result<T> {

        private static final EqualsAndHashCode<Success> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Success.class)
                .compare(Success::getValue)
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
        public <U> Success<U> map(Function<T, U> mappingFunction) {
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
    }

    public static final class Failure<T> extends Result<T> {

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
        public <U> Failure<U> map(Function<T, U> mappingFunction) {
            return Result.failure();
        }
    }
}
