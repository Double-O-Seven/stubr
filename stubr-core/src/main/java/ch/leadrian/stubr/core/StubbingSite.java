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

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface to indicate where an instance for a certain type is being stubbed. Common sites are {@link
 * ch.leadrian.stubr.core.site.ParameterStubbingSite} such as {@link ch.leadrian.stubr.core.site.MethodParameterStubbingSite}
 * or {@link ch.leadrian.stubr.core.site.ConstructorStubbingSite}. Further implementations can be found in {@link
 * ch.leadrian.stubr.core.site.StubbingSites}.
 * <p>
 * {@link Matcher}s may use to {@code StubbingSite} to determine whether a given {@link Stubber} should be applied.
 *
 * @see ch.leadrian.stubr.core.site.StubbingSites
 */
public interface StubbingSite {

    /**
     * Returns the parent of a {@code StubbingSite}.
     * <p>
     * Given the following two constructors for example:
     * <pre>
     * public Foo(Bar bar) {
     *     // ...
     * }
     *
     * public Bar(int value) {
     *     // ...
     * }
     * </pre>
     * If an instance of {@code Foo} is stubbed using {@link StubbingStrategies#constructor()}, it would require an
     * instance of {@code Bar} to create an instance. If {@code Bar} in turn is also created using {@link
     * StubbingStrategies#constructor()} then {@code Bar} instances would be created at a {@link
     * ch.leadrian.stubr.core.site.ConstructorParameterStubbingSite}. The parent of {@code Bar}'s site would be whatever
     * {@link StubbingSite} is the site of {@code Foo}.
     *
     * @return the parent site
     */
    Optional<? extends StubbingSite> getParent();

    /**
     * This method returns an ordered {@link Stream} of all transitive parents of and including {@code this}.
     * <p>
     * {@code this} will be the first element in the stream, followed by its parent, grandparent and so on until there
     * is a parent without a parent.
     *
     * @return a stream of all transitive parents of {@code this}
     */
    default Stream<StubbingSite> walk() {
        return getParent()
                .map(parent -> Stream.concat(Stream.of(this), parent.walk()))
                .orElseGet(() -> Stream.of(this));
    }

}
