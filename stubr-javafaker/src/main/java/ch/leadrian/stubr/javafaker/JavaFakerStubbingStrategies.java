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

import ch.leadrian.stubr.core.StubbingStrategy;
import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static ch.leadrian.stubr.javafaker.FakerStrategies.city;
import static ch.leadrian.stubr.javafaker.FakerStrategies.country;
import static ch.leadrian.stubr.javafaker.FakerStrategies.firstName;
import static ch.leadrian.stubr.javafaker.FakerStrategies.lastName;
import static ch.leadrian.stubr.javafaker.FakerStrategies.phoneNumber;
import static ch.leadrian.stubr.javafaker.FakerStrategies.street;
import static ch.leadrian.stubr.javafaker.FakerStrategies.zipCode;
import static java.util.Objects.requireNonNull;

public final class JavaFakerStubbingStrategies {

    private static final class RandomHolder {

        static final Random INSTANCE = new Random(System.currentTimeMillis());

    }

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private JavaFakerStubbingStrategies() {
    }

    private static FakerStubbingStrategy faked(FakerStrategy strategy, Faker faker) {
        requireNonNull(faker, "faker");
        return new FakerStubbingStrategy(faker, strategy);
    }

    public static StubbingStrategy faked(FakerStrategy strategy, Random random, Locale locale) {
        requireNonNull(strategy, "strategy");
        requireNonNull(random, "random");
        requireNonNull(locale, "locale");
        return faked(strategy, new Faker(locale, random));
    }

    public static StubbingStrategy faked(FakerStrategy strategy, Locale locale) {
        return faked(strategy, RandomHolder.INSTANCE, locale);
    }

    public static StubbingStrategy faked(FakerStrategy strategy, Random random) {
        return faked(strategy, random, DEFAULT_LOCALE);
    }

    public static StubbingStrategy faked(FakerStrategy strategy) {
        return faked(strategy, RandomHolder.INSTANCE);
    }

    public static List<StubbingStrategy> fakedData(Random random, Locale locale) {
        return ImmutableList.of(
                faked(firstName(), random, locale),
                faked(lastName(), random, locale),
                faked(phoneNumber(), random, locale),
                faked(street(), random, locale),
                faked(city(), random, locale),
                faked(zipCode(), random, locale),
                faked(country(), random, locale)
        );
    }

    public static List<StubbingStrategy> fakedData(Random random) {
        return fakedData(random, DEFAULT_LOCALE);
    }

    public static List<StubbingStrategy> fakedData(Locale locale) {
        return fakedData(RandomHolder.INSTANCE, locale);
    }

    public static List<StubbingStrategy> fakedData() {
        return fakedData(RandomHolder.INSTANCE);
    }

}
