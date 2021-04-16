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

package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class ProvidedValueStrategy implements StubbingStrategy {

    private final Map<Type, ValueProvider> valueProviders;

    ProvidedValueStrategy(Map<Type, ValueProvider> valueProviders) {
        requireNonNull(valueProviders, "valueProviders");
        this.valueProviders = new HashMap<>(valueProviders);
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        ValueProvider valueProvider = valueProviders.get(type);
        return valueProvider != null && valueProvider.hasValue();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return valueProviders.get(type).get();
    }

}
