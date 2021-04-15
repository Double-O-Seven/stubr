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

/**
 * An interface that allows to query a {@link Stubber} for a given type even after an first matching {@link
 * StubbingStrategy} has provided a stub value.
 * <p>
 * This interface is useful for intercepting stubs and enhancing them for example before finally returning them.
 *
 * @see ch.leadrian.stubr.core.strategy.EnhancingStubbingStrategy
 * @see ch.leadrian.stubr.core.strategy.StubbingStrategies#fieldInjection(Matcher)
 * @see ch.leadrian.stubr.core.strategy.StubbingStrategies#methodInjection(Matcher)
 */
public interface StubberChain {

    /**
     * Returns {@code true} if there's a {@link StubbingStrategy} that accepts the requested type to stub.
     *
     * @return {@code true} if there's a {@link StubbingStrategy} that accepts the requested type to stub
     */
    boolean hasNext();

    /**
     * Returns a {@link Result} provided by the next accepting {@link StubbingStrategy}.
     *
     * @return a {@link Result} provided by the next accepting {@link StubbingStrategy}
     */
    Result<?> next();

}
