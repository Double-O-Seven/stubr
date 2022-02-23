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

package ch.leadrian.stubr.core.site;

import java.lang.reflect.Method;

/**
 * Stubbing site indicating that the current stubbing site is a {@link Method}.
 */
public interface MethodStubbingSite extends ExecutableStubbingSite, NamedStubbingSite {

    /**
     * Returns the method where the stubbing takes place.
     *
     * @return the method
     */
    Method getMethod();

    /**
     * {@inheritDoc}
     */
    @Override
    default Method getExecutable() {
        return getMethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default String getName() {
        return getMethod().getName();
    }

}
