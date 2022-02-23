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

package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

/**
 * A class used by {@link ch.leadrian.stubr.junit.annotation.Include} to determine which {@link Stubber}s should be
 * included when configuring a {@link Stubber} for a test case.
 * <p>
 * Any implementation must provide a default constructor since all instances are instantiated through reflection.
 *
 * @see ch.leadrian.stubr.junit.annotation.Include
 */
public interface StubberProvider {

    /**
     * Returns a list of {@link Stubber}s that should be included when configuring a {@link Stubber} for a test case.
     * <p>
     * The {@link Stubber}s will be included in the {@link Stubber} in the order in which they are present in the given
     * list.
     *
     * @param extensionContext the current extension context
     * @return a list of {@link Stubber}s that should be included when configuring a {@link Stubber} for a test case
     * @see ch.leadrian.stubr.core.StubberBuilder#include(Stubber)
     */
    List<? extends Stubber> getStubbers(ExtensionContext extensionContext);

}
