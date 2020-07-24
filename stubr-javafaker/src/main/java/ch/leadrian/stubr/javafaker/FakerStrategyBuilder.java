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

package ch.leadrian.stubr.javafaker;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A builder for building a {@link FakerStrategy} using a default implementation.
 */
public final class FakerStrategyBuilder {

    private final List<String[]> acceptedWords = new ArrayList<>();

    FakerStrategyBuilder() {
    }

    /**
     * Adds a sequence of words accepted by the built {@link FakerStrategy}. {@link
     * WordSequence#containsInSequence(String...)} is used to determine whether the words are accepted.
     *
     * @param words the word sequence that is accepted
     * @return {@code this}
     * @see WordSequence#containsInSequence(String...)
     */
    public FakerStrategyBuilder accept(String... words) {
        requireNonNull(words, "words");
        acceptedWords.add(words.clone());
        return this;
    }

    /**
     * Returns a new {@link FakerStrategy} that uses the given {@code fakerFunction} to provide a stub value.
     *
     * @param fakerFunction the {@link FakerFunction} used to provide a stub value
     * @return a new {@link FakerStrategy} that uses the given {@code fakerFunction} to provide a stub value
     */
    public FakerStrategy build(FakerFunction fakerFunction) {
        requireNonNull(fakerFunction, "fakerFunction");
        return new DefaultFakerStrategy(acceptedWords, fakerFunction);
    }

    /**
     * Returns a new {@link FakerStrategy} that uses the given {@code fakerFunction} to provide a stub value.
     *
     * @param fakerFunction the {@link Function} used to provide a stub value
     * @return a new {@link FakerStrategy} that uses the given {@code fakerFunction} to provide a stub value
     */
    public FakerStrategy build(Function<? super Faker, String> fakerFunction) {
        requireNonNull(fakerFunction, "fakerFunction");
        return build((faker, wordSequence, context) -> fakerFunction.apply(faker));
    }

}
