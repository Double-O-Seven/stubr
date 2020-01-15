package ch.leadrian.stubr.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

abstract class ResultProvider {

    static ResultProvider of(Object... values) {
        if (values.length == 0) {
            return NoValue.INSTANCE;
        } else if (values.length == 1) {
            return new ConstantValue(values[0]);
        } else {
            return new ValueSequence(asList(values));
        }
    }

    private ResultProvider() {
    }

    abstract Result<?> get();

    abstract ResultProvider getUntouchedInstance();

    private static class NoValue extends ResultProvider {

        static final NoValue INSTANCE = new NoValue();

        @Override
        Result<?> get() {
            return Result.failure();
        }

        @Override
        ResultProvider getUntouchedInstance() {
            return this;
        }
    }

    private static class ConstantValue extends ResultProvider {

        private final Object value;

        private ConstantValue(Object value) {
            this.value = value;
        }

        @Override
        Result<?> get() {
            return Result.success(value);
        }

        @Override
        ResultProvider getUntouchedInstance() {
            return this;
        }
    }

    private static class ValueSequence extends ResultProvider {

        private final List<?> originalValues;
        private final LinkedList<Result<?>> results;

        private ValueSequence(Collection<?> values) {
            requireNonNull(values, "values");
            this.originalValues = new ArrayList<>(values);
            this.results = values.stream().map(Result::success).collect(toCollection(LinkedList::new));
        }

        @Override
        public Result<?> get() {
            Result<?> result = results.poll();
            if (result == null) {
                throw new AssertionError("Exhausted results");
            }
            return result;
        }

        @Override
        ResultProvider getUntouchedInstance() {
            return new ValueSequence(originalValues);
        }
    }

}
