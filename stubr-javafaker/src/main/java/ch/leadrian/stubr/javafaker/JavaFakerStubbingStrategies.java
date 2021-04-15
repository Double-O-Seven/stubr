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

import ch.leadrian.stubr.core.StubbingStrategy;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

import static ch.leadrian.stubr.internal.com.google.common.collect.ImmutableList.toImmutableList;
import static ch.leadrian.stubr.javafaker.FakerStrategies.city;
import static ch.leadrian.stubr.javafaker.FakerStrategies.country;
import static ch.leadrian.stubr.javafaker.FakerStrategies.firstName;
import static ch.leadrian.stubr.javafaker.FakerStrategies.lastName;
import static ch.leadrian.stubr.javafaker.FakerStrategies.phoneNumber;
import static ch.leadrian.stubr.javafaker.FakerStrategies.street;
import static ch.leadrian.stubr.javafaker.FakerStrategies.zipCode;
import static java.util.Objects.requireNonNull;

/**
 * A collection of {@link StubbingStrategy}s based on {@link Faker}.
 */
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

    /**
     * Returns a new {@link StubbingStrategy} that uses a {@link Faker}.
     *
     * @param strategy the strategy applied for {@link Faker}
     * @param random   the random instance used to randomly select a value
     * @param locale   the locale for which a value is selected
     * @return a new {@link StubbingStrategy} that uses a {@link Faker}
     */
    public static StubbingStrategy faked(FakerStrategy strategy, Random random, Locale locale) {
        requireNonNull(strategy, "strategy");
        requireNonNull(random, "random");
        requireNonNull(locale, "locale");
        return faked(strategy, new Faker(locale, random));
    }

    /**
     * Returns a new {@link StubbingStrategy} that uses a {@link Faker}.
     *
     * @param strategy the strategy applied for {@link Faker}
     * @param locale   the locale for which a value is selected
     * @return a new {@link StubbingStrategy} that uses a {@link Faker}
     */
    public static StubbingStrategy faked(FakerStrategy strategy, Locale locale) {
        return faked(strategy, RandomHolder.INSTANCE, locale);
    }

    /**
     * Returns a new {@link StubbingStrategy} that uses a {@link Faker}.
     * <p>
     * By default, English is used as locale.
     *
     * @param strategy the strategy applied for {@link Faker}
     * @param random   the random instance used to randomly select a value
     * @return a new {@link StubbingStrategy} that uses a {@link Faker}
     */
    public static StubbingStrategy faked(FakerStrategy strategy, Random random) {
        return faked(strategy, random, DEFAULT_LOCALE);
    }

    /**
     * Returns a new {@link StubbingStrategy} that uses a {@link Faker}.
     * <p>
     * By default, English is used as locale.
     *
     * @param strategy the strategy applied for {@link Faker}
     * @return a new {@link StubbingStrategy} that uses a {@link Faker}
     */
    public static StubbingStrategy faked(FakerStrategy strategy) {
        return faked(strategy, RandomHolder.INSTANCE);
    }

    /**
     * A list of {@link StubbingStrategy} for faking the following types of data:
     * <ul>
     *     <li>First name</li>
     *     <li>Last name</li>
     *     <li>Phone number</li>
     *     <li>Street</li>
     *     <li>City</li>
     *     <li>ZIP code</li>
     *     <li>Country</li>
     * </ul>
     *
     * @param random the random instance used to randomly select a value
     * @param locale the locale for which a value is selected
     * @return a list of {@link StubbingStrategy} that use a {@link Faker}
     */
    public static List<StubbingStrategy> fakedData(Random random, Locale locale) {
        return Stream.of(
                firstName(),
                lastName(),
                phoneNumber(),
                street(),
                city(),
                zipCode(),
                country()
        )
                .map(strategy -> faked(strategy, random, locale))
                .collect(toImmutableList());
    }

    /**
     * A list of {@link StubbingStrategy} for faking the following types of data:
     * <ul>
     *     <li>First name</li>
     *     <li>Last name</li>
     *     <li>Phone number</li>
     *     <li>Street</li>
     *     <li>City</li>
     *     <li>ZIP code</li>
     *     <li>Country</li>
     * </ul>
     * <p>
     * By default, English is used as locale.
     *
     * @param random the random instance used to randomly select a value
     * @return a list of {@link StubbingStrategy} that use a {@link Faker}
     */
    public static List<StubbingStrategy> fakedData(Random random) {
        return fakedData(random, DEFAULT_LOCALE);
    }

    /**
     * A list of {@link StubbingStrategy} for faking the following types of data:
     * <ul>
     *     <li>First name</li>
     *     <li>Last name</li>
     *     <li>Phone number</li>
     *     <li>Street</li>
     *     <li>City</li>
     *     <li>ZIP code</li>
     *     <li>Country</li>
     * </ul>
     *
     * @param locale the locale for which a value is selected
     * @return a list of {@link StubbingStrategy} that use a {@link Faker}
     */
    public static List<StubbingStrategy> fakedData(Locale locale) {
        return fakedData(RandomHolder.INSTANCE, locale);
    }

    /**
     * A list of {@link StubbingStrategy} for faking the following types of data:
     * <ul>
     *     <li>First name</li>
     *     <li>Last name</li>
     *     <li>Phone number</li>
     *     <li>Street</li>
     *     <li>City</li>
     *     <li>ZIP code</li>
     *     <li>Country</li>
     * </ul>
     * <p>
     * By default, English is used as locale.
     *
     * @return a list of {@link StubbingStrategy} that use a {@link Faker}
     */
    public static List<StubbingStrategy> fakedData() {
        return fakedData(RandomHolder.INSTANCE);
    }

}
