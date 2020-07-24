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

import ch.leadrian.stubr.core.StubbingContext;
import com.github.javafaker.Faker;

import java.util.List;

import static java.util.stream.Collectors.toList;

final class DefaultFakerStrategy implements FakerStrategy {

    private final List<String[]> acceptedWords;
    private final FakerFunction delegate;

    DefaultFakerStrategy(List<String[]> acceptedWords, FakerFunction delegate) {
        this.acceptedWords = acceptedWords.stream().map(String[]::clone).collect(toList());
        this.delegate = delegate;
    }

    @Override
    public boolean accepts(WordSequence words) {
        return acceptedWords.stream().anyMatch(words::containsInSequence);
    }

    @Override
    public String fake(Faker faker, WordSequence words, StubbingContext context) {
        return delegate.fake(faker, words, context);
    }

}
