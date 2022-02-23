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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static ch.leadrian.stubr.core.selector.Selectors.first;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class EnumValueStubbingStrategy extends SimpleStubbingStrategy<Object> {

    static final EnumValueStubbingStrategy FIRST_ELEMENT = new EnumValueStubbingStrategy(first());

    private final Selector<Enum<?>> selector;

    EnumValueStubbingStrategy(Selector<Enum<?>> selector) {
        requireNonNull(selector, "selector");
        this.selector = selector;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type.isEnum() && selectValue(context, type).isPresent();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return false;
    }

    @Override
    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
        return false;
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return selectValue(context, type).orElseThrow(() -> new StubbingException(context.getSite(), type));
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        throw new StubbingException(context.getSite(), type);
    }

    @Override
    protected Object stubGenericArrayType(StubbingContext context, GenericArrayType type) {
        throw new StubbingException(context.getSite(), type);
    }

    private Optional<? extends Enum<?>> selectValue(StubbingContext context, Class<?> type) {
        List<Enum<?>> values = stream(type.getEnumConstants())
                .map(value -> (Enum<?>) value)
                .collect(toList());
        return selector.select(context, values);
    }

}
