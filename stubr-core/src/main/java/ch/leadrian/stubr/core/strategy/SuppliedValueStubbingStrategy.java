/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

final class SuppliedValueStubbingStrategy extends SimpleStubbingStrategy<Object> {

    private final Type valueType;
    private final StubValueSupplier<?> valueSupplier;
    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    SuppliedValueStubbingStrategy(Type valueType, StubValueSupplier<?> valueSupplier) {
        requireNonNull(valueType, "valueType");
        requireNonNull(valueSupplier, "valueSupplier");
        this.valueType = valueType;
        this.valueSupplier = valueSupplier;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return valueType == type;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return valueType.equals(type);
    }

    @Override
    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
        return valueType.equals(type);
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return getNextValue(context);
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return getNextValue(context);
    }

    @Override
    protected Object stubGenericArrayType(StubbingContext context, GenericArrayType type) {
        return getNextValue(context);
    }

    private Object getNextValue(StubbingContext context) {
        return valueSupplier.get(context, sequenceNumber.getAndIncrement());
    }

}
