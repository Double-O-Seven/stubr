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
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class CollectionStubbingStrategy<T extends Collection> extends SimpleStubbingStrategy<T> {

    private final Class<T> collectionClass;
    private final Function<List<Object>, ? extends T> collectionFactory;
    private final ToIntFunction<? super StubbingContext> collectionSize;

    CollectionStubbingStrategy(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, ToIntFunction<? super StubbingContext> collectionSize) {
        requireNonNull(collectionClass, "collectionClass");
        requireNonNull(collectionFactory, "collectionFactory");
        requireNonNull(collectionSize, "collectionSize");
        this.collectionClass = collectionClass;
        this.collectionFactory = collectionFactory;
        this.collectionSize = collectionSize;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return collectionClass == type && collectionSize.applyAsInt(context) == 0;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return collectionClass == type.getRawType() && type.getActualTypeArguments().length == 1;
    }

    @Override
    protected boolean acceptsGenericArrayType(StubbingContext context, GenericArrayType type) {
        return false;
    }

    @Override
    protected T stubClass(StubbingContext context, Class<?> type) {
        return collectionFactory.apply(emptyList());
    }

    @Override
    protected T stubParameterizedType(StubbingContext context, ParameterizedType type) {
        Type valueType = type.getActualTypeArguments()[0];
        StubbingSite site = StubbingSites.parameterizedType(context.getSite(), type, 0);
        List<Object> values = IntStream.iterate(0, i -> i + 1)
                .limit(collectionSize.applyAsInt(context))
                .mapToObj(i -> context.getStubber().stub(valueType, site))
                .collect(toList());
        return collectionFactory.apply(values);
    }

    @Override
    protected T stubGenericArrayType(StubbingContext context, GenericArrayType type) {
        throw new StubbingException(context.getSite(), type);
    }

}
