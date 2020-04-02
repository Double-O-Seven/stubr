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

package ch.leadrian.stubr.junit.annotation;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubberBuilder;
import ch.leadrian.stubr.core.Stubbers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ch.leadrian.stubr.core.Stubbers.defaultStubber;
import static ch.leadrian.stubr.core.Stubbers.minimalStubber;

/**
 * Annotation defining which {@link Stubber} should be used as a baseline for the {@link Stubber} used in a test case.
 * If a {@link Stubber} is used as a baseline, it will be included using {@link StubberBuilder#include(Stubber)}.
 * <p>
 * There are three different variants that can be selected:
 * <ul>
 * <li>{@link Variant#DEFAULT}</li>
 * <li>{@link Variant#MINIMAL}</li>
 * <li>{@link Variant#EMPTY}</li>
 * </ul>
 * By default {@link Variant#DEFAULT} will be used.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface StubberBaseline {

    /**
     * The variant of the baseline.
     *
     * @return the variant
     */
    Variant value();

    /**
     * Enum with a factory method providing a {@link StubberBuilder} that include the baseline {@link Stubber}.
     */
    enum Variant {
        /**
         * Uses {@link Stubbers#defaultStubber()} as a baseline.
         */
        DEFAULT {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder().include(defaultStubber());
            }
        },
        /**
         * Uses {@link Stubbers#minimalStubber()} as a baseline.
         */
        MINIMAL {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder().include(minimalStubber());
            }
        },
        /**
         * Uses no {@link Stubber} as a baseline. If no additional configuration with {@link Include} or {@link
         * StubWith} is add to the test case, the stubbing will fail.
         */
        EMPTY {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder();
            }
        };

        /**
         * Returns a {@link StubberBuilder} that may include a baseline {@link Stubber}. The returned {@link
         * StubberBuilder} will be used to build the {@link Stubber} for a test case.
         *
         * @return a {@link StubberBuilder}
         */
        public abstract StubberBuilder getBuilder();
    }

}
