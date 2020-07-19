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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class WordSequenceTest {

    @Nested
    class ContainsTest {

        @ParameterizedTest
        @ArgumentsSource(PositiveContainsArgumentsProvider.class)
        void givenContainedWordItShouldReturnTrue(String source, String containedWord) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean contains = wordSequence.contains(containedWord);

            assertThat(contains)
                    .isTrue();
        }

        @ParameterizedTest
        @ArgumentsSource(NegativeContainsArgumentsProvider.class)
        void givenNotContainedWordItShouldReturnTrue(String source, String containedWord) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean contains = wordSequence.contains(containedWord);

            assertThat(contains)
                    .isFalse();
        }

    }

    @Nested
    class ContainsInSequenceTest {

        @ParameterizedTest
        @ArgumentsSource(PositiveContainsInSequenceArgumentsProvider.class)
        void givenContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInSequence = wordSequence.containsInSequence(containedWords);

            assertThat(ContainsInSequence)
                    .isTrue();
        }

        @ParameterizedTest
        @ArgumentsSource(NegativeContainsInSequenceArgumentsProvider.class)
        void givenNotContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInSequence = wordSequence.containsInSequence(containedWords);

            assertThat(ContainsInSequence)
                    .isFalse();
        }

    }

    @Nested
    class ContainsInOrderTest {

        @ParameterizedTest
        @ArgumentsSource(PositiveContainsInOrderArgumentsProvider.class)
        void givenContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInOrder = wordSequence.containsInOrder(containedWords);

            assertThat(ContainsInOrder)
                    .isTrue();
        }

        @ParameterizedTest
        @ArgumentsSource(NegativeContainsInOrderArgumentsProvider.class)
        void givenNotContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInOrder = wordSequence.containsInOrder(containedWords);

            assertThat(ContainsInOrder)
                    .isFalse();
        }

    }

    @Nested
    class ContainsInAnyOrderTest {

        @ParameterizedTest
        @ArgumentsSource(PositiveContainsInAnyOrderArgumentsProvider.class)
        void givenContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInAnyOrder = wordSequence.containsInAnyOrder(containedWords);

            assertThat(ContainsInAnyOrder)
                    .isTrue();
        }

        @ParameterizedTest
        @ArgumentsSource(NegativeContainsInAnyOrderArgumentsProvider.class)
        void givenNotContainedWordItShouldReturnTrue(String source, String[] containedWords) {
            WordSequence wordSequence = WordSequence.extractFrom(source);

            boolean ContainsInAnyOrder = wordSequence.containsInAnyOrder(containedWords);

            assertThat(ContainsInAnyOrder)
                    .isFalse();
        }

    }

    private static class PositiveContainsArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", "get"),
                    arguments("getFirstName", "first"),
                    arguments("getFirstName", "name"),
                    arguments("GetFirstName", "get"),
                    arguments("getFirstName", "GET"),
                    arguments("getFirstName", "FIRST"),
                    arguments("getFirstName", "NAME")
            );
        }

    }

    private static class NegativeContainsArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", "getFirst"),
                    arguments("getFirstName", "FirstName"),
                    arguments("getFirstName", "getFirstName")
            );
        }

    }

    private static class PositiveContainsInSequenceArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"first"}),
                    arguments("getFirstName", new String[]{"name"}),
                    arguments("GetFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"GET"}),
                    arguments("getFirstName", new String[]{"FIRST"}),
                    arguments("getFirstName", new String[]{"NAME"}),
                    arguments("getFirstName", new String[]{"get", "first", "name"}),
                    arguments("getFirstName", new String[]{"get", "first"}),
                    arguments("getFirstName", new String[]{"first", "name"})
            );
        }

    }

    private static class NegativeContainsInSequenceArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"getFirst"}),
                    arguments("getFirstName", new String[]{"FirstName"}),
                    arguments("getFirstName", new String[]{"getFirstName"}),
                    arguments("getFirstName", new String[]{"get", "name"}),
                    arguments("getFirstName", new String[]{"get", "name", "first"})
            );
        }

    }

    private static class PositiveContainsInOrderArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"first"}),
                    arguments("getFirstName", new String[]{"name"}),
                    arguments("GetFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"GET"}),
                    arguments("getFirstName", new String[]{"FIRST"}),
                    arguments("getFirstName", new String[]{"NAME"}),
                    arguments("getFirstName", new String[]{"get", "first", "name"}),
                    arguments("getFirstName", new String[]{"get", "first"}),
                    arguments("getFirstName", new String[]{"get", "name"}),
                    arguments("getFirstName", new String[]{"first", "name"})
            );
        }

    }

    private static class NegativeContainsInOrderArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"getFirst"}),
                    arguments("getFirstName", new String[]{"FirstName"}),
                    arguments("getFirstName", new String[]{"getFirstName"}),
                    arguments("getFirstName", new String[]{"get", "name", "first"})
            );
        }

    }

    private static class PositiveContainsInAnyOrderArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"first"}),
                    arguments("getFirstName", new String[]{"name"}),
                    arguments("GetFirstName", new String[]{"get"}),
                    arguments("getFirstName", new String[]{"GET"}),
                    arguments("getFirstName", new String[]{"FIRST"}),
                    arguments("getFirstName", new String[]{"NAME"}),
                    arguments("getFirstName", new String[]{"get", "first", "name"}),
                    arguments("getFirstName", new String[]{"get", "first"}),
                    arguments("getFirstName", new String[]{"get", "name"}),
                    arguments("getFirstName", new String[]{"first", "name"}),
                    arguments("getFirstName", new String[]{"get", "name", "first"})
            );
        }

    }

    private static class NegativeContainsInAnyOrderArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("getFirstName", new String[]{"getFirst"}),
                    arguments("getFirstName", new String[]{"FirstName"}),
                    arguments("getFirstName", new String[]{"getFirstName"})
            );
        }

    }

}