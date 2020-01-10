package ch.leadrian.stubr.core;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public abstract class Result<T> {

    public static <T> Success<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Failure<T> failure() {
        return new Failure<>();
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
            return new Success<>(mappingFunction.apply(value));
        }

        @Override
        public int hashCode() {
            return 31 * Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Success)) {
                return false;
            }

            Success<?> other = (Success<?>) obj;
            return Objects.equals(this.value, other.value);
        }
    }

    public static final class Failure<T> extends Result<T> {

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
            return new Failure<>();
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Failure;
        }
    }
}
