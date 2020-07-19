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
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class FakerStrategyBuilder {

    private final List<String[]> acceptedWords = new ArrayList<>();

    FakerStrategyBuilder() {
    }

    public FakerStrategyBuilder accept(String... words) {
        requireNonNull(words, "words");
        acceptedWords.add(words.clone());
        return this;
    }

    public FakerStrategy build(BiFunction<? super Faker, ? super WordSequence, String> fakerFunction) {
        requireNonNull(fakerFunction, "fakerFunction");
        return new DefaultFakerStrategy(acceptedWords, fakerFunction);
    }

    public FakerStrategy build(Function<? super Faker, String> fakerFunction) {
        requireNonNull(fakerFunction, "fakerFunction");
        return build((faker, wordSequence) -> fakerFunction.apply(faker));
    }

}
