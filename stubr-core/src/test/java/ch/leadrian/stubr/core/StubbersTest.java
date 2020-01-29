package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StubbersTest {

    @Nested
    class DefaultRootStubbingStrategyTest {

        private Stubber stubber = RootStubbers.defaultRootStubber();

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