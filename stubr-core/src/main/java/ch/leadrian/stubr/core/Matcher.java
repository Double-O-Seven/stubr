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

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.lang.reflect.Type;

/**
 * An interface used to determine whether a {@link StubbingStrategy} can be used to stub an instance of a certain type.
 * A matcher may be used to extend the conditions under which a {@link StubbingStrategy} can be applied. A matcher may
 * also be used to determine a suitable constructor or factory method to instantiate a stubbed instance.
 * <p>
 * Various implementations can be found in {@link ch.leadrian.stubr.core.matcher.Matchers}.
 *
 * @param <T> type that is accepted by the {@code Matcher}.
 * @see ch.leadrian.stubr.core.matcher.Matchers
 * @see StubbingStrategy#accepts(StubbingContext, Type)
 * @see StubbingStrategy#when(Matcher)
 * @see StubbingStrategies#constructor(Matcher)
 * @see StubbingStrategies#factoryMethod(Matcher)
 */
@FunctionalInterface
public interface Matcher<T> {

    /**
     * A matcher may match depending on the given {@link StubbingContext} or the given {@code value} of type {@link T}.
     * If the {@code context} is evaluated, most likely only it's {@link StubbingSite} is relevant, as the referenced
     * {@link Stubber} does not contain any publicly available state. However, the full context is being passed for sake
     * of extensibility.
     *
     * @param context {@link StubbingContext} in which a {@link StubbingStrategy} is applied.
     * @param value   value that may be evaluated by the matcher.
     * @return {@code true} if the matcher matches the {@code value} of type {@link T}, else {@code false}
     */
    boolean matches(StubbingContext context, T value);

    /**
     * Combines two matchers into one.
     *
     * @param other the other matcher
     * @return a new matcher that matches if and only if both matchers {@code this} and {@code other} match
     */
    default Matcher<T> and(Matcher<? super T> other) {
        return (context, value) -> this.matches(context, value) && other.matches(context, value);
    }

    /**
     * Combines two matchers into one.
     *
     * @param other the other matcher
     * @return a new matcher that matches if at least one of the matchers {@code this} and {@code other} match
     */
    default Matcher<T> or(Matcher<? super T> other) {
        return (context, value) -> this.matches(context, value) || other.matches(context, value);
    }

    /**
     * Creates a new matcher that logically negates {@code this} matcher.
     *
     * @return a new matcher that matches if and only if {@code this} matcher does not match
     * @see ch.leadrian.stubr.core.matcher.Matchers#not(Matcher)
     */
    default Matcher<T> negate() {
        return (context, value) -> !matches(context, value);
    }

}
