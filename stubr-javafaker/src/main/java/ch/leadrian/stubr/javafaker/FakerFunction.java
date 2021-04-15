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

package ch.leadrian.stubr.javafaker;

import ch.leadrian.stubr.core.StubbingContext;
import com.github.javafaker.Faker;

/**
 * A functional interface that typically provides a string value by using a {@link Faker}.
 *
 * @see FakerStrategy
 * @see FakerStrategyBuilder
 */
@FunctionalInterface
public interface FakerFunction {

    /**
     * @param faker   the {@link Faker} used to provide a fake value
     * @param words   the words extracted from a {@link ch.leadrian.stubr.core.site.NamedStubbingSite} describing what
     *                is being faked
     * @param context the current stubbing context
     * @return a string value typically provided by the given {@link Faker}
     */
    String fake(Faker faker, WordSequence words, StubbingContext context);

}
