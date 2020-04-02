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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ImplementationStubbingStrategy extends SimpleStubbingStrategy<Object> {

    private final Type targetType;
    private final Type implementationType;

    ImplementationStubbingStrategy(Type targetType, Type implementationType) {
        requireNonNull(targetType, "targetType");
        requireNonNull(implementationType, "implementationType");
        this.targetType = targetType;
        this.implementationType = implementationType;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return targetType == type;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return targetType.equals(type);
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return stub(context);
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return stub(context);
    }

    private Object stub(StubbingContext context) {
        return context.getStubber().stub(implementationType, context.getSite());
    }

}
