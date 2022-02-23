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

package ch.leadrian.stubr.javafaker;

/**
 * A strategy defining how a {@link com.github.javafaker.Faker} is used to provide stub values.
 *
 * @see JavaFakerStubbingStrategies#faked(FakerStrategy)
 * @see JavaFakerStubbingStrategies#faked(FakerStrategy, java.util.Locale)
 * @see JavaFakerStubbingStrategies#faked(FakerStrategy, java.util.Random)
 * @see JavaFakerStubbingStrategies#faked(FakerStrategy, java.util.Random, java.util.Locale)
 */
public interface FakerStrategy extends FakerFunction {

    /**
     * Factory method for creating a {@link FakerStrategyBuilder}.
     *
     * @return a new {@link FakerStrategyBuilder}
     */
    static FakerStrategyBuilder builder() {
        return new FakerStrategyBuilder();
    }

    /**
     * Returns {@code true} if the strategy accepts a given word sequence, else {@code false}.
     *
     * @param words the words that may be accepted
     * @return {@code true} if the strategy accepts a given word sequence, else {@code false}
     */
    boolean accepts(WordSequence words);

}
