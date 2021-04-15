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

/**
 * An enum defining the different stubbing modes for {@link java.util.Optional}s.
 */
public enum OptionalStubbingMode {
    /**
     * All {@link java.util.Optional}s are stubbed using {@link java.util.Optional#empty()}.
     */
    EMPTY(OptionalStubbingStrategy.EMPTY),
    /**
     * All {@link java.util.Optional}s require a suitable stub value.
     */
    PRESENT(OptionalStubbingStrategy.PRESENT),
    /**
     * {@link java.util.Optional}s for which a suitable stub value can be provided, will be stubbed using {@link
     * java.util.Optional#of}, else {@link java.util.Optional#empty()} will be used.
     */
    PRESENT_IF_POSSIBLE(OptionalStubbingStrategy.PRESENT_IF_POSSIBLE);

    private final OptionalStubbingStrategy strategy;

    OptionalStubbingMode(OptionalStubbingStrategy strategy) {
        this.strategy = strategy;
    }

    OptionalStubbingStrategy getStrategy() {
        return strategy;
    }
}
