package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RootStubbersTest {

    @Nested
    class DefaultRootStubberTest {

        private RootStubber rootStubber = RootStubbers.defaultRootStubber();

        @Test
        void shouldStubEnum() {
            Foo value = rootStubber.stub(Foo.class);

            assertThat(value)
                    .isEqualTo(Foo.INSTANCE);
        }

        @Test
        void shouldStubInterface() {
            Bar value = rootStubber.stub(Bar.class);

            assertThat(value.getInt())
                    .isZero();
        }

        @Test
        void shouldStubListOfStrings() {
            List<Integer> value = rootStubber.stub(new TypeLiteral<List<Integer>>() {
            });

            assertThat(value)
                    .containsExactly(0);
        }

        @Test
        void shouldStubWithFactoryMethod() {
            CreatedWithFactoryMethod value = rootStubber.stub(CreatedWithFactoryMethod.class);

            assertThat(value.isCreatedWithFactoryMethod())
                    .isTrue();
        }

        @Test
        void shouldStubWithNonDefaultConstructor() {
            CreatedWithNonDefaultConstructor value = rootStubber.stub(CreatedWithNonDefaultConstructor.class);

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