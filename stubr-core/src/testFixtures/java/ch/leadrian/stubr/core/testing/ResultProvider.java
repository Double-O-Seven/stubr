/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Result;

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
