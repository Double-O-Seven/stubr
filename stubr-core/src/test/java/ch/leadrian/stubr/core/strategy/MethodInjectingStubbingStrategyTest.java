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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static ch.leadrian.stubr.core.matcher.Matchers.equalTo;
import static ch.leadrian.stubr.core.matcher.Matchers.mappedTo;
import static ch.leadrian.stubr.core.matcher.Matchers.method;
import static ch.leadrian.stubr.core.matcher.Matchers.site;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.constantValue;
import static com.google.common.base.MoreObjects.toStringHelper;

class MethodInjectingStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testMethodInjectingStubbingStrategy() {
        Person expectedPerson = new Person();
        expectedPerson.setFirstName("Hans");
        expectedPerson.setLastName("Wurst");
        expectedPerson.setFullName("Hans Wurst");
        expectedPerson.setAge(69);
        return stubbingStrategyTester()
                .provideStubsWith(Stubber.builder()
                        .stubWith(constantValue("Hans").when(site(method(mappedTo(Method::getName, equalTo("setFirstName"))))))
                        .stubWith(constantValue("Wurst").when(site(method(mappedTo(Method::getName, equalTo("setLastName"))))))
                        .build())
                .provideStub(new Person())
                .provideStub(int.class, 69, 1337)
                .accepts(Person.class)
                .andStubs(expectedPerson)
                .test(StubbingStrategies.methodInjection((context, method) -> method.isAnnotationPresent(Inject.class)));
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testMethodInjectingStubbingStrategyWithGenericType() {
        return stubbingStrategyTester()
                .provideStubsWith(Stubber.builder()
                        .stubWith(constantValue("fubar").when(site(method(mappedTo(Method::getName, equalTo("setFoo"))))))
                        .build())
                .provideStub(new TypeLiteral<Foo<String>>() {}, new Foo<>())
                .accepts(new TypeLiteral<Foo<String>>() {})
                .andStubs(new Foo<>("fubar"))
                .test(StubbingStrategies.methodInjection((context, method) -> method.isAnnotationPresent(Inject.class)));
    }

    static abstract class LivingBeing {

        private int age;

        public int getAge() {
            return age;
        }

        @Inject
        public void setAge(int age) {
            this.age = age;
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Inject {}

    static class Person extends LivingBeing {

        @SuppressWarnings("unused")
        @Inject
        public static void shouldNotBeInjected(String message) {
            throw new IllegalStateException(message);
        }

        private static final EqualsAndHashCode<Person> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Person.class)
                .compareAndHash(Person::getFirstName)
                .compareAndHash(Person::getLastName)
                .compareAndHash(Person::getFullName)
                .compareAndHash(LivingBeing::getAge)
                .build();

        private String firstName;

        private String lastName;

        private String fullName;

        public String getFirstName() {
            return firstName;
        }

        @Inject
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        @Inject
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        // Make sure overridden method is not called twice
        @Inject
        @Override
        public void setAge(int age) {
            super.setAge(age);
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

        @Inject
        public void setFoo(T foo) {
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