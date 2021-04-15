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
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.getRawType;

enum NullValueStubbingStrategy implements StubbingStrategy {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(clazz -> !clazz.isPrimitive())
                .isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return null;
    }
}
