/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StubbersTest {

    @Nested
    class DefaultRootStubbingStrategyTest {

        private Stubber stubber = Stubbers.defaultStubber();

        @Test
        void shouldStubEnum() {
            Foo value = stubber.stub(Foo.class);

            assertThat(value)
                    .isEqualTo(Foo.INSTANCE);
        }

        @Test
        void shouldStubInterface() {
            Bar value = stubber.stub(Bar.class);

            assertThat(value.getInt())
                    .isZero();
        }

        @Test
        void shouldStubListOfStrings() {
            List<Integer> value = stubber.stub(new TypeLiteral<List<Integer>>() {});

            assertThat(value)
                    .containsExactly(0);
        }

        @Test
        void shouldStubWithFactoryMethod() {
            CreatedWithFactoryMethod value = stubber.stub(CreatedWithFactoryMethod.class);

            assertThat(value.isCreatedWithFactoryMethod())
                    .isTrue();
        }

        @Test
        void shouldStubWithNonDefaultConstructor() {
            CreatedWithNonDefaultConstructor value = stubber.stub(CreatedWithNonDefaultConstructor.class);

            assertThat(value.isCreatedWithNonDefaultConstructor())
                    .isTrue();
        }

    }

    enum Foo {
        INSTANCE
    }

    interface Bar {

        int getInt();

    }

    @SuppressWarnings("unused")
    public static class CreatedWithFactoryMethod {

        private boolean createdWithFactoryMethod = false;

        public static CreatedWithFactoryMethod create() {
            CreatedWithFactoryMethod instance = new CreatedWithFactoryMethod();
            instance.createdWithFactoryMethod = true;
            return instance;
        }

        public CreatedWithFactoryMethod() {
        }

        public boolean isCreatedWithFactoryMethod() {
            return createdWithFactoryMethod;
        }

    }

    @SuppressWarnings("unused")
    public static class CreatedWithNonDefaultConstructor {

        private boolean createdWithNonDefaultConstructor = false;

        public CreatedWithNonDefaultConstructor() {
        }

        public CreatedWithNonDefaultConstructor(String value) {
            this.createdWithNonDefaultConstructor = true;
        }

        public boolean isCreatedWithNonDefaultConstructor() {
            return createdWithNonDefaultConstructor;
        }

    }

}