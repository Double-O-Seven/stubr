/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

abstract class ValueProvider {

    static ValueProvider of(Object... values) {
        if (values.length == 0) {
            return NoValue.INSTANCE;
        } else if (values.length == 1) {
            return new ConstantValue(values[0]);
        } else {
            return new ValueSequence(asList(values));
        }
    }

    private ValueProvider() {
    }

    abstract boolean hasValue();

    abstract Object get();

    abstract ValueProvider getUntouchedInstance();

    private static class NoValue extends ValueProvider {

        static final NoValue INSTANCE = new NoValue();

        @Override
        boolean hasValue() {
            return false;
        }

        @Override
        Object get() {
            throw new AssertionError("Not supported");
        }

        @Override
        ValueProvider getUntouchedInstance() {
            return this;
        }

    }

    private static class ConstantValue extends ValueProvider {

        private final Object value;

        private ConstantValue(Object value) {
            this.value = value;
        }

        @Override
        boolean hasValue() {
            return true;
        }

        @Override
        Object get() {
            return value;
        }

        @Override
        ValueProvider getUntouchedInstance() {
            return this;
        }

    }

    private static class ValueSequence extends ValueProvider {

        private final List<?> originalValues;
        private final LinkedList<Object> results;

        private ValueSequence(Collection<?> values) {
            requireNonNull(values, "values");
            this.originalValues = new ArrayList<>(values);
            this.results = new LinkedList<>(values);
        }

        @Override
        boolean hasValue() {
            return results.peek() != null;
        }

        @Override
        public Object get() {
            Object result = results.poll();
            if (result == null) {
                throw new AssertionError("Exhausted results");
            }
            return result;
        }

        @Override
        ValueProvider getUntouchedInstance() {
            return new ValueSequence(originalValues);
        }

    }

}
