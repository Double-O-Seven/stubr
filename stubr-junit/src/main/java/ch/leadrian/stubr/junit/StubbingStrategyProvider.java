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

package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

/**
 * A class used by {@link ch.leadrian.stubr.junit.annotation.StubWith} to determine which {@link StubbingStrategy}s
 * should be used when configuring a {@link Stubber} for a test case.
 * <p>
 * Any implementation must provide a default constructor since all instances are instantiated through reflection.
 *
 * @see ch.leadrian.stubr.junit.annotation.Include
 */
public interface StubbingStrategyProvider {

    /**
     * Returns a list of {@link StubbingStrategy}s that should be included when configuring a {@link Stubber} for a test
     * case.
     * <p>
     * The {@link StubbingStrategy}s will be added to the {@link Stubber} in the order in which they are present in the
     * given list.
     *
     * @param extensionContext the current extension context
     * @return a list of {@link StubbingStrategy}s that should be used when configuring a {@link Stubber} for a test
     * case
     * @see ch.leadrian.stubr.core.StubberBuilder#stubWith(StubbingStrategy)
     */
    List<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext extensionContext);

}
