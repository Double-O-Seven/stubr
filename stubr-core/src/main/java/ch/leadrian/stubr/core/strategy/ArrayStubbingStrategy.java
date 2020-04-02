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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.site.ArrayStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.function.ToIntFunction;

import static java.util.Objects.requireNonNull;

final class ArrayStubbingStrategy extends SimpleStubbingStrategy<Object> {

    private final ToIntFunction<? super StubbingContext> arraySize;

    ArrayStubbingStrategy(ToIntFunction<? super StubbingContext> arraySize) {
        requireNonNull(arraySize, "arraySize");
        this.arraySize = arraySize;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type.isArray();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return false;
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        Class<?> componentType = type.getComponentType();
        Object array = Array.newInstance(componentType, arraySize.applyAsInt(context));
        ArrayStubbingSite site = StubbingSites.array(context.getSite(), componentType);
        int length = getArrayLength(array);
        for (int i = 0; i < length; i++) {
            setArrayValue(array, i, context.getStubber().stub(componentType, site));
        }
        return array;
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        throw new StubbingException(context.getSite(), type);
    }

    private int getArrayLength(Object array) {
        if (array instanceof boolean[]) {
            return ((boolean[]) array).length;
        } else if (array instanceof byte[]) {
            return ((byte[]) array).length;
        } else if (array instanceof short[]) {
            return ((short[]) array).length;
        } else if (array instanceof char[]) {
            return ((char[]) array).length;
        } else if (array instanceof int[]) {
            return ((int[]) array).length;
        } else if (array instanceof long[]) {
            return ((long[]) array).length;
        } else if (array instanceof float[]) {
            return ((float[]) array).length;
        } else if (array instanceof double[]) {
            return ((double[]) array).length;
        } else if (array instanceof Object[]) {
            return ((Object[]) array).length;
        } else {
            throw new StubbingException(String.format("Not an array: %s", array));
        }
    }

    private void setArrayValue(Object array, int index, Object value) {
        if (array instanceof boolean[]) {
            ((boolean[]) array)[index] = (Boolean) value;
        } else if (array instanceof byte[]) {
            ((byte[]) array)[index] = (Byte) value;
        } else if (array instanceof short[]) {
            ((short[]) array)[index] = (Short) value;
        } else if (array instanceof char[]) {
            ((char[]) array)[index] = (Character) value;
        } else if (array instanceof int[]) {
            ((int[]) array)[index] = (Integer) value;
        } else if (array instanceof long[]) {
            ((long[]) array)[index] = (Long) value;
        } else if (array instanceof float[]) {
            ((float[]) array)[index] = (Float) value;
        } else if (array instanceof double[]) {
            ((double[]) array)[index] = (Double) value;
        } else if (array instanceof Object[]) {
            ((Object[]) array)[index] = value;
        } else {
            throw new StubbingException(String.format("Not an array: %s", array));
        }
    }

}
