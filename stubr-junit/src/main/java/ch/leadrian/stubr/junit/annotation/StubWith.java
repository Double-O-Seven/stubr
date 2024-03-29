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

package ch.leadrian.stubr.junit.annotation;

import ch.leadrian.stubr.junit.StubberProvider;
import ch.leadrian.stubr.junit.StubbingStrategyProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation signaling that the given {@link StubbingStrategyProvider} should be used to configure a {@link
 * ch.leadrian.stubr.core.Stubber} for a test case.
 * <p>
 * For the annotation to work, the test must be extended with {@link ch.leadrian.stubr.junit.Stubr}.
 *
 * @see StubberProvider
 * @see ch.leadrian.stubr.junit.Stubr
 * @see org.junit.jupiter.api.extension.ExtendWith
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface StubWith {

    /**
     * The class of the {@link StubbingStrategyProvider}. The {@link ch.leadrian.stubr.core.Stubber}s provided by the
     * provider will be used in the {@link ch.leadrian.stubr.core.Stubber} configured for a test case.
     *
     * @return class of {@link StubbingStrategyProvider}
     * @see StubbingStrategyProvider
     */
    Class<? extends StubbingStrategyProvider>[] value();

}
