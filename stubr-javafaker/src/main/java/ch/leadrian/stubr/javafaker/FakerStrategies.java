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

public final class FakerStrategies {

    private static final FakerStrategy FIRST_NAME = FakerStrategy.builder()
            .accept("first", "name")
            .build(faker -> faker.name().firstName());

    private static final FakerStrategy LAST_NAME = FakerStrategy.builder()
            .accept("last", "name")
            .accept("surname")
            .build(faker -> faker.name().lastName());

    private static final FakerStrategy PHONE_NUMBER = FakerStrategy.builder()
            .accept("phone", "number")
            .accept("phonenumber")
            .build(faker -> faker.phoneNumber().phoneNumber());

    private static final FakerStrategy STREET = FakerStrategy.builder()
            .accept("street")
            .build(faker -> faker.address().streetName());

    private static final FakerStrategy CITY = FakerStrategy.builder()
            .accept("city")
            .accept("town")
            .build(faker -> faker.address().city());

    private static final FakerStrategy ZIP_CODE = FakerStrategy.builder()
            .accept("zip", "code")
            .accept("zipcode")
            .build(faker -> faker.address().zipCode());

    private static final FakerStrategy COUNTRY = FakerStrategy.builder()
            .accept("country")
            .build(faker -> faker.address().country());

    private FakerStrategies() {
    }

    public static FakerStrategy firstName() {
        return FIRST_NAME;
    }

    public static FakerStrategy lastName() {
        return LAST_NAME;
    }

    public static FakerStrategy phoneNumber() {
        return PHONE_NUMBER;
    }

    public static FakerStrategy street() {
        return STREET;
    }

    public static FakerStrategy city() {
        return CITY;
    }

    public static FakerStrategy zipCode() {
        return ZIP_CODE;
    }

    public static FakerStrategy country() {
        return COUNTRY;
    }

}
