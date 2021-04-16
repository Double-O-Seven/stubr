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

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

/**
 * A base class for stubbing strategies that may delegate the creation of the requested stub value further downstream
 * and then only perform enhancing operations on the stub value.
 *
 * @see StubbingContext#hasResult()
 * @see StubbingContext#result()
 */
public abstract class EnhancingStubbingStrategy implements StubbingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean accepts(StubbingContext context, Type type) {
        return context.getNext().filter(StubbingContext::hasResult).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object stub(StubbingContext context, Type type) {
        Result<?> result = context.getNext().map(StubbingContext::result).orElse(Result.failure());
        if (result.isFailure()) {
            throw new StubbingException(context.getSite(), type);
        }

        Object stubValue = result.getValue();
        return enhance(context, type, stubValue);
    }

    /**
     * Enhances the given {@code stubValue}. Additional operations may be performed on the given {@code stubValue} and
     * then the {@code stubValue} may be returned. Alternatively, a new stub value may be returned that is based on the
     * given {@code stubValue} or a completely new stub value may be returned.
     * <p>
     * The only requirement for the returned value is that it is an instance of the given {@code Type}.
     *
     * @param context   the context of the stub request
     * @param type      the requested stub value type
     * @param stubValue the stub value that was provided by the {@link StubbingStrategy} of {@link
     *                  StubbingContext#getNext()}
     * @return the enhanced stub value
     */
    protected abstract Object enhance(StubbingContext context, Type type, Object stubValue);

}
