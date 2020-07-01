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
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.MemoizingStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class MemoizingStubbingStrategy implements StubbingStrategy {

    private final Map<Type, Object> memoizedStubsByType = new HashMap<>();
    private final StubbingStrategy delegate;

    MemoizingStubbingStrategy(StubbingStrategy delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return delegate.accepts(context, type);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        Object value = memoizedStubsByType.get(type);
        if (value == null) {
            value = computeValue(context, type);
            memoizedStubsByType.put(type, value);
        }
        return value;
    }

    private Object computeValue(StubbingContext context, Type type) {
        MemoizingStubbingSite site = StubbingSites.memoizing(context.getSite());
        return delegate.stub(context.fork(site), type);
    }

}
