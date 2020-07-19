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

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static java.util.Arrays.stream;
import static java.util.Collections.indexOfSubList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class WordSequence {

    private final List<String> words;

    public static WordSequence extractFrom(String value) {
        List<String> words = ImmutableList.copyOf(LOWER_CAMEL.to(LOWER_HYPHEN, value).split("-"));
        return new WordSequence(words);
    }

    private WordSequence(List<String> words) {
        this.words = words;
    }

    public boolean contains(String word) {
        return words.contains(word.toLowerCase());
    }

    public boolean containsInSequence(String... words) {
        List<String> lowerCaseWords = transform(words);
        return indexOfSubList(this.words, lowerCaseWords) != -1;
    }

    public boolean containsInOrder(String... words) {
        List<String> lowerCaseWords = transform(words);
        Map<String, Integer> wordIndices = lowerCaseWords.stream().collect(toMap(identity(), this.words::indexOf));
        int currentIndex = 0;
        for (String word : lowerCaseWords) {
            int index = wordIndices.get(word);
            if (index < currentIndex) {
                return false;
            }
            currentIndex = index;
        }
        return true;
    }

    public boolean containsInAnyOrder(String... words) {
        return this.words.containsAll(transform(words));
    }

    private List<String> transform(String[] values) {
        return stream(values).map(String::toLowerCase).collect(toList());
    }

}
