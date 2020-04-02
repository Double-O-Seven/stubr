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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.AnnotatedElement;

/**
 * Stubbing site with an {@link AnnotatedElement}.
 *
 * @see ConstructorParameterStubbingSite
 * @see MethodParameterStubbingSite
 * @see MethodReturnValueStubbingSite
 */
public interface AnnotatedStubbingSite extends StubbingSite {

    /**
     * Returns the {@link AnnotatedElement} at the stubbing site.
     * <p>
     * This might be the {@link java.lang.reflect.Parameter} of a {@link java.lang.reflect.Constructor} or {@link
     * java.lang.reflect.Method} or the {@link java.lang.reflect.Method} itself in case of a {@link
     * java.lang.reflect.Proxy} stubbing for example.
     *
     * @return the annotated element
     */
    AnnotatedElement getAnnotatedElement();

}
