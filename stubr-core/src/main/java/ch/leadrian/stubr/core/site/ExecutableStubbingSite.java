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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Executable;

/**
 * Stubbing site indicating that the current stubbing site is an {@link Executable}. Implementations of {@link
 * Executable} are {@link java.lang.reflect.Constructor} and {@link java.lang.reflect.Method}.
 */
public interface ExecutableStubbingSite extends StubbingSite {

    /**
     * Returns the {@link Executable} where the stubbing takes place.
     *
     * @return the executable
     */
    Executable getExecutable();

}
