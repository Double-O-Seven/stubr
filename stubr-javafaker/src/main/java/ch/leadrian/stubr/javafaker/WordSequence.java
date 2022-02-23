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

import ch.leadrian.stubr.internal.com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

import static ch.leadrian.stubr.internal.com.google.common.base.CaseFormat.LOWER_CAMEL;
import static ch.leadrian.stubr.internal.com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static java.util.Arrays.stream;
import static java.util.Collections.indexOfSubList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class represents a sequence of words that is extracted from a lower camel-case identifier, such as a method name
 * or parameter name for example. All words are stored in lower case.
 * <p>
 * Calling {@link WordSequence#extractFrom(String)} with a string value "getFirstName" for example results in a sequence
 * containing "get", "first" and "name".
 */
public final class WordSequence {

    private final List<String> words;

    /**
     * Returns the word sequence extracted from the given {@code value}.
     * <p>
     * Passing a string value "getFirstName" for example results in a sequence containing "get", "first" and "name".
     *
     * @param value the passed string representing of a camel-case identifier.
     * @return the word sequence extracted from the given {@code value}
     */
    public static WordSequence extractFrom(String value) {
        List<String> words = ImmutableList.copyOf(LOWER_CAMEL.to(LOWER_HYPHEN, value).split("-"));
        return new WordSequence(words);
    }

    private WordSequence(List<String> words) {
        this.words = words;
    }

    /**
     * Returns {@code true} if the word sequence contains the given word, else {@code false}.
     * <p>
     * The check is performed case-insensitively.
     *
     * @param word the word possibly contained in the word sequence
     * @return {@code true} if the word sequence contains the given word, else {@code false}
     */
    public boolean contains(String word) {
        return words.contains(word.toLowerCase());
    }

    /**
     * Returns {@code true} if the word sequence contains the given words in their exact sequence, one after another
     * without any other words in between, else {@code false}.
     * <p>
     * The check is performed case-insensitively.
     * <p>
     * A word sequence consisting of "get", "first" and "name" will return {@code true} when one of the following words
     * are passed in the given order.
     * <ul>
     *     <li>"get"</li>
     *     <li>"first"</li>
     *     <li>"name"</li>
     *     <li>"get" and "first"</li>
     *     <li>"first" and "name</li>
     *     <li>"get", "first" and "name</li>
     * </ul>
     * For any other word sequence, {@code false} would be returned.
     *
     * @param words the words possibly contained in the word sequence
     * @return {@code true} if the word sequence contains the given words in their exact sequence, one after another
     * without any other words in between, else {@code false}
     */
    public boolean containsInSequence(String... words) {
        List<String> lowerCaseWords = toLowerCase(words);
        return indexOfSubList(this.words, lowerCaseWords) != -1;
    }

    /**
     * Returns {@code true} if the word sequence contains the given words in the given order. There may be additional
     * words in between the given words.
     * <p>
     * The check is performed case-insensitively.
     * <p>
     * A word sequence consisting of "get", "first" and "name" will return {@code true} when one of the following words
     * are passed in the given order.
     * <ul>
     *     <li>"get"</li>
     *     <li>"first"</li>
     *     <li>"name"</li>
     *     <li>"get" and "first"</li>
     *     <li>"get" and "name"</li>
     *     <li>"first" and "name</li>
     *     <li>"get", "first" and "name</li>
     * </ul>
     * For any other word sequence, {@code false} would be returned.
     *
     * @param words the words possibly contained in the word sequence
     * @return {@code true} if the word sequence contains the given words in the given order
     */
    public boolean containsInOrder(String... words) {
        List<String> lowerCaseWords = toLowerCase(words);
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

    /**
     * Returns {@code true} if the word sequence contains the given words in any order.
     * <p>
     * A word sequence consisting of "get", "first" and "name" will return {@code true} for any combination and order of
     * the three given words.
     *
     * @param words the words possibly contained in the word sequence
     * @return {@code true} if the word sequence contains the given words in any order
     */
    public boolean containsInAnyOrder(String... words) {
        return this.words.containsAll(toLowerCase(words));
    }

    private static List<String> toLowerCase(String[] values) {
        return stream(values).map(String::toLowerCase).collect(toList());
    }

}
