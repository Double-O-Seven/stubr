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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.equalizer.EqualsAndHashCode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static com.google.common.base.MoreObjects.toStringHelper;

class FieldInjectingStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testDefaultEnumValueStubber() {
        Person expectedPerson = new Person();
        expectedPerson.setFirstName("Hans");
        expectedPerson.setLastName("Wurst");
        expectedPerson.setFullName("Hans Wurst");
        return stubbingStrategyTester()
                .provideStub(new Person())
                .provideStub(String.class, "Hans", "Wurst")
                .accepts(Person.class)
                .andStubs(expectedPerson)
                .test(StubbingStrategies.fieldInjection((context, field) -> !"fullName".equals(field.getName())));
    }

    static class Person {

        private static final EqualsAndHashCode<Person> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Person.class)
                .compareAndHash(Person::getFirstName)
                .compareAndHash(Person::getLastName)
                .compareAndHash(Person::getFullName)
                .build();

        private String firstName;

        private String lastName;

        private String fullName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getFullName() {
            if (fullName == null) {
                fullName = firstName + " " + lastName;
            }
            return fullName;
        }

        @Override
        public boolean equals(Object o) {
            return EQUALS_AND_HASH_CODE.equals(this, o);
        }

        @Override
        public int hashCode() {
            return EQUALS_AND_HASH_CODE.hashCode(this);
        }

        @Override
        public String toString() {
            return toStringHelper(this)
                    .add("firstName", firstName)
                    .add("lastName", lastName)
                    .add("fullName", fullName)
                    .toString();
        }

    }

}