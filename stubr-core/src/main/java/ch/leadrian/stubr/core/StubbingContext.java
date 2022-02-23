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

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeResolver;

import java.lang.reflect.Type;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * This class is a container for contextual information during a stubbing process.
 * <p>
 * Included are the {@link Stubber} performing the stubbing as well as the {@link StubbingSite} which indicates where
 * the requested stub value will be used.
 */
public final class StubbingContext {

    private final Stubber stubber;
    private final StubbingSite site;
    private final TypeResolver typeResolver;
    private final Type type;
    private final StubbingStrategy strategy;
    private StubbingContext next;

    StubbingContext(Stubber stubber, StubbingSite site, Type type, StubbingStrategy strategy) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        requireNonNull(type, "type");
        this.stubber = stubber;
        this.site = site;
        this.typeResolver = TypeResolver.using(type);
        this.type = type;
        this.strategy = strategy;
    }

    /**
     * @return the {@link Stubber} performing the stubbing
     */
    public Stubber getStubber() {
        return stubber;
    }

    /**
     * @return the {@link StubbingSite} where the requested stub value will be used
     */
    public StubbingSite getSite() {
        return site;
    }

    /**
     * @return the {@link TypeResolver} that is used to resolve types
     */
    public TypeResolver getTypeResolver() {
        return typeResolver;
    }

    /**
     * @return the context that may be applied afterwards
     */
    public Optional<StubbingContext> getNext() {
        return Optional.ofNullable(next);
    }

    void setNext(StubbingContext next) {
        this.next = next;
    }

    /**
     * @return {@code true} if this {@link StubbingContext} can provide a result, else {@code false}
     * @see ch.leadrian.stubr.core.strategy.EnhancingStubbingStrategy
     */
    public boolean hasResult() {
        return strategy != null && strategy.accepts(this, type);
    }

    /**
     * @return the {@link Result} computed in this context
     * @see ch.leadrian.stubr.core.strategy.EnhancingStubbingStrategy
     */
    public Result<?> result() {
        return strategy != null ? Result.success(strategy.stub(this, type)) : Result.failure();
    }

}
