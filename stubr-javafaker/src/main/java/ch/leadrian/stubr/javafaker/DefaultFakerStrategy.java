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

import java.util.List;
import java.util.function.BiFunction;

final class DefaultFakerStrategy implements FakerStrategy {

    private final List<String[]> acceptedWords;
    private final BiFunction<Faker, WordSequence, String> fakerFunction;

    DefaultFakerStrategy(List<String[]> acceptedWords, BiFunction<Faker, WordSequence, String> fakerFunction) {
        this.acceptedWords = acceptedWords;
        this.fakerFunction = fakerFunction;
    }

    @Override
    public boolean accepts(WordSequence words) {
        return acceptedWords.stream().anyMatch(words::containsInSequence);
    }

    @Override
    public String fake(Faker faker, WordSequence words) {
        return fakerFunction.apply(faker, words);
    }

}
