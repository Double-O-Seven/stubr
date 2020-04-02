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

import ch.leadrian.stubr.core.strategy.OptionalStubbingMode;
import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.nullable;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.commonConstantValues;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.commonSuppliedValues;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.defaultCollections;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.defaultConstructor;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.defaultValue;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.emptyDefaultCollections;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.enumValue;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.factoryMethod;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.nonDefaultConstructor;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.nullValue;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.optional;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.proxy;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.stubber;

/**
 * Class containing factory methods for basic {@link Stubber}s.
 */
public final class Stubbers {

    private Stubbers() {
    }

    /**
     * Provides a stateless {@link Stubber} that will stub the following stub values:
     * <ul>
     * <li>Default values for primitives and their wrappers</li>
     * <li>{@link java.lang.reflect.Proxy} instances for interfaces that return stub values for non-void method
     * calls</li>
     * <li>Enum values</li>
     * <li>Non-empty {@link java.util.Optional}s</li>
     * <li>Object that can be created using a default constructor</li>
     * <li>Object that can be created using a non-default constructor</li>
     * <li>Object that can be created using a factory method</li>
     * <li>Common collections and array, filled with a single element</li>
     * <li>Reasonable constant values for commonly used immutable classes such as {@link java.time.LocalDate}</li>
     * <li>Reasonable constant values for commonly used mutable classes such as {@link java.util.Date}</li>
     * </ul>
     *
     * @return a {@link Stubber} stubbing non-null, non-empty default values.
     * @see StubbingStrategies
     */
    public static Stubber defaultStubber() {
        return Stubber.builder()
                .stubWith(defaultValue())
                .stubWith(proxy())
                .stubWith(enumValue())
                .stubWith(optional(OptionalStubbingMode.PRESENT))
                .stubWith(defaultConstructor())
                .stubWith(nonDefaultConstructor())
                .stubWith(factoryMethod())
                .stubWith(defaultCollections(1))
                .stubWith(stubber())
                .stubWith(commonConstantValues())
                .stubWith(commonSuppliedValues())
                .build();
    }

    /**
     * Provides a stateless {@link Stubber} that uses {@link Stubbers#defaultStubber()} as a baseline. However, default
     * collections, {@link java.util.Optional}s and arrays are kept empty and nullable sites will receive {@code null}
     * as stub value.
     *
     * @return a {@link Stubber} stubbing nullable, empty default values.
     * @see StubbingStrategies
     */
    public static Stubber minimalStubber() {
        return Stubber.builder()
                .include(defaultStubber())
                .stubWith(nullValue().when(annotatedSiteIs(nullable())))
                .stubWith(optional(OptionalStubbingMode.EMPTY))
                .stubWith(emptyDefaultCollections())
                .build();
    }

}
