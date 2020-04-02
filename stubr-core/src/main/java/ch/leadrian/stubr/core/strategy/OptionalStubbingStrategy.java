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

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

abstract class OptionalStubbingStrategy extends SimpleStubbingStrategy<Optional<Object>> {

    static final OptionalStubbingStrategy EMPTY = new Empty();
    static final OptionalStubbingStrategy PRESENT = new Present();
    static final OptionalStubbingStrategy PRESENT_IF_POSSIBLE = new PresentIfPossible();

    private OptionalStubbingStrategy() {
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return Optional.class == type && isEmptyAllowed();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType parameterizedType) {
        return Optional.class == parameterizedType.getRawType();
    }

    @Override
    protected Optional<Object> stubClass(StubbingContext context, Class<?> type) {
        return Optional.empty();
    }

    @Override
    protected Optional<Object> stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return stubOptional(context, type);
    }

    protected abstract Optional<Object> stubOptional(StubbingContext context, ParameterizedType type);

    protected abstract boolean isEmptyAllowed();

    private static final class Empty extends OptionalStubbingStrategy {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, ParameterizedType type) {
            return Optional.empty();
        }

        @Override
        protected boolean isEmptyAllowed() {
            return true;
        }

    }

    private static abstract class AbstractPresent extends OptionalStubbingStrategy {

        @Override
        protected final Optional<Object> stubOptional(StubbingContext context, ParameterizedType type) {
            StubbingSite site = StubbingSites.parameterizedType(context.getSite(), type, 0);
            Type valueType = type.getActualTypeArguments()[0];
            return stubOptional(context, site, valueType);
        }

        protected abstract Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType);

    }

    private static final class Present extends AbstractPresent {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType) {
            return Optional.ofNullable(context.getStubber().stub(valueType, site));
        }

        @Override
        protected boolean isEmptyAllowed() {
            return false;
        }

    }

    private static final class PresentIfPossible extends AbstractPresent {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType) {
            Result<?> result = context.getStubber().tryToStub(valueType, site);
            if (result.isSuccess()) {
                return Optional.ofNullable(result.getValue());
            }
            return Optional.empty();
        }

        @Override
        protected boolean isEmptyAllowed() {
            return true;
        }

    }

}
