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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static ch.leadrian.stubr.core.matcher.Matchers.equalTo;
import static ch.leadrian.stubr.core.matcher.Matchers.field;
import static ch.leadrian.stubr.core.matcher.Matchers.mappedTo;
import static ch.leadrian.stubr.core.matcher.Matchers.site;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.constantValue;
import static com.google.common.base.MoreObjects.toStringHelper;
import static org.assertj.core.api.Assertions.assertThat;

class FieldInjectingStubbingStrategyTest {

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testFieldInjectingStubbingStrategy() {
        Person expectedPerson = new Person();
        expectedPerson.setFirstName("Hans");
        expectedPerson.setLastName("Wurst");
        expectedPerson.setFullName("Hans Wurst");
        expectedPerson.setAge(69);
        return stubbingStrategyTester()
                .provideStub(new Person())
                .provideStubsWith(Stubber.builder()
                        .stubWith(constantValue("Hans").when(site(field(mappedTo(Field::getName, equalTo("firstName"))))))
                        .stubWith(constantValue("Wurst").when(site(field(mappedTo(Field::getName, equalTo("lastName"))))))
                        .build())
                .provideStub(int.class, 69)
                .accepts(Person.class)
                .andStubSatisfies(value -> {
                    assertThat(value)
                            .isInstanceOfSatisfying(Person.class, person -> {
                                assertThat(person).isEqualTo(expectedPerson);
                                assertThat(person.getSocialSecurityNumber()).isEqualTo(1337L);
                            });
                    assertThat(Person.numberOfPeopleInSwitzerland).isEqualTo(8_570_000);
                })
                .test(StubbingStrategies.fieldInjection((context, field) -> !"fullName".equals(field.getName())));
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testFieldInjectingStubbingStrategyWithGenericType() {
        return stubbingStrategyTester()
                .provideStub(new Person())
                .provideStubsWith(Stubber.builder()
                        .stubWith(constantValue("fubar").when(site(field(mappedTo(Field::getName, equalTo("foo"))))))
                        .build())
                .provideStub(new TypeLiteral<Foo<String>>() {}, new Foo<>())
                .accepts(new TypeLiteral<Foo<String>>() {})
                .andStubs(new Foo<>("fubar"))
                .test(StubbingStrategies.fieldInjection((context, field) -> field.getDeclaringClass() == Foo.class));
    }

    static abstract class LivingBeing {

        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

    static class Person extends LivingBeing {

        static int numberOfPeopleInSwitzerland = 8_570_000;

        private static final EqualsAndHashCode<Person> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Person.class)
                .compareAndHash(Person::getFirstName)
                .compareAndHash(Person::getLastName)
                .compareAndHash(Person::getFullName)
                .compareAndHash(LivingBeing::getAge)
                .compareAndHash(Person::getSocialSecurityNumber)
                .build();

        private String firstName;

        private String lastName;

        private String fullName;

        private final long socialSecurityNumber = 1337;

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

        public long getSocialSecurityNumber() {
            return socialSecurityNumber;
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
                    .add("age", getAge())
                    .add("socialSecurityNumber", socialSecurityNumber)
                    .toString();
        }

    }

    static class Foo<T> {

        @SuppressWarnings("rawtypes")
        private static final EqualsAndHashCode<Foo> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Foo.class)
                .compareAndHash(foo -> foo.foo)
                .build();

        private T foo;

        Foo() {
        }

        Foo(T foo) {
            this.foo = foo;
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
                    .add("foo", foo)
                    .toString();
        }

    }

}