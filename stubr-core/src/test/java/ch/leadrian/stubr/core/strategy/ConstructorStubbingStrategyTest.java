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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.equalizer.Equals;
import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.TestStubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsBuilder;
import static ch.leadrian.stubr.core.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.stream.Collectors.toList;

class ConstructorStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testConstructorStubberWithFilter() throws Exception {
        return stubbingStrategyTester()
                .provideStub("test")
                .provideStub(int.class, 1337)
                .accepts(UnambiguousPublicConstructor.class)
                .andStubs(new UnambiguousPublicConstructor("test", 1337))
                .at(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .accepts(UnambiguousProtectedConstructor.class)
                .andStubs(new UnambiguousProtectedConstructor("test", 1337))
                .at(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .accepts(UnambiguousPackagePrivateConstructor.class)
                .andStubs(new UnambiguousPackagePrivateConstructor("test", 1337))
                .at(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .accepts(new TypeLiteral<GenericPublicConstructor<String>>() {
                })
                .andStubs(new GenericPublicConstructor<>("test", 1337))
                .at(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, GenericPublicConstructor.class.getDeclaredConstructor(Object.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, GenericPublicConstructor.class.getDeclaredConstructor(Object.class, int.class), 1)
                )
                .rejects(UnambiguousPrivateConstructor.class)
                .rejects(AmbiguousPublicConstructor.class)
                .rejects(AmbiguousProtectedConstructor.class)
                .rejects(AmbiguousPackagePrivateConstructor.class)
                .rejects(NotMatchingConstructor.class)
                .rejects(UnambiguousConstructorOfAbstractClass.class)
                .rejects(Foo.class)
                .rejects(Bar.class)
                .rejects(int.class)
                .test(
                        StubbingStrategies.constructor((Matcher<Constructor<?>>) (context, constructor) -> constructor.getParameterCount() == 2),
                        StubbingStrategies.constructor((Selector<Constructor<?>>) (context, constructors) -> {
                            List<? extends Constructor<?>> filteredValues = constructors.stream()
                                    .filter(constructor -> constructor.getParameterCount() == 2)
                                    .collect(toList());
                            return Optional.ofNullable(filteredValues.size() == 1 ? filteredValues.get(0) : null);
                        })
                );
    }

    @TestFactory
    Stream<DynamicTest> testConstructorStubberAcceptingAnyMethod() throws Exception {
        return stubbingStrategyTester()
                .provideStub("test")
                .provideStub(int.class, 1337)
                .accepts(UnambiguousPublicConstructor.class)
                .andStubs(new UnambiguousPublicConstructor("test", 1337))
                .at(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .rejects(MultiplePublicConstructors.class)
                .test(StubbingStrategies.constructor());
    }

    @SuppressWarnings("unused")
    private static class MultiplePublicConstructors {

        public MultiplePublicConstructors(String stringValue, int intValue) {
        }

        public MultiplePublicConstructors(String stringValue, long longValue) {
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPublicConstructor {

        private static final Equals<UnambiguousPublicConstructor> EQUALS = equalsBuilder(UnambiguousPublicConstructor.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        public UnambiguousPublicConstructor(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class GenericPublicConstructor<T> {

        @SuppressWarnings("rawtypes")
        private static final Equals<GenericPublicConstructor> EQUALS = equalsBuilder(GenericPublicConstructor.class)
                .compare(value -> value.value)
                .build();

        private final T value;

        public GenericPublicConstructor(T value, int intValue) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousProtectedConstructor {

        private static final Equals<UnambiguousProtectedConstructor> EQUALS = equalsBuilder(UnambiguousProtectedConstructor.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        protected UnambiguousProtectedConstructor(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPackagePrivateConstructor {

        private static final Equals<UnambiguousPackagePrivateConstructor> EQUALS = equalsBuilder(UnambiguousPackagePrivateConstructor.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        UnambiguousPackagePrivateConstructor(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPrivateConstructor {

        private UnambiguousPrivateConstructor(String stringValue, int intValue) {
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousPublicConstructor {

        public AmbiguousPublicConstructor(String stringValue, int intValue) {
        }

        public AmbiguousPublicConstructor(String stringValue, float intValue) {
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousProtectedConstructor {

        protected AmbiguousProtectedConstructor(String stringValue, int intValue) {
        }

        protected AmbiguousProtectedConstructor(String stringValue, float intValue) {
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousPackagePrivateConstructor {

        AmbiguousPackagePrivateConstructor(String stringValue, int intValue) {
        }

        AmbiguousPackagePrivateConstructor(String stringValue, float intValue) {
        }

    }

    @SuppressWarnings("unused")
    private static class NotMatchingConstructor {

        NotMatchingConstructor(String stringValue, int intValue, Object objectValue) {
        }

    }

    @SuppressWarnings("unused")
    private static abstract class UnambiguousConstructorOfAbstractClass {

        public UnambiguousConstructorOfAbstractClass(String stringValue, int intValue) {
        }

    }

    private enum Foo {
    }

    private interface Bar {
    }

}