package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import ch.leadrian.stubr.core.type.Types;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OptionalStubberTest {

    private StubbingContext context;
    private RootStubber rootStubber;

    @BeforeEach
    void setUp() {
        rootStubber = mock(RootStubber.class);
        context = new StubbingContext(rootStubber, StubbingSites.unknown());
    }

    @Nested
    class EmptyTest {

        @Test
        void shouldAcceptOptionalWithoutTypeArguments() {
            boolean accepts = OptionalStubber.EMPTY.accepts(context, Optional.class);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithoutTypeArguments() {
            Object stub = OptionalStubber.EMPTY.stub(context, Optional.class);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).isEmpty());
        }

        @Test
        void shouldAcceptOptionalWithTypeArguments() {
            Type type = new TypeLiteral<Optional<Float>>() {
            }.getType();

            boolean accepts = OptionalStubber.EMPTY.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithTypeArgument() {
            Type type = new TypeLiteral<Optional<Float>>() {
            }.getType();

            Object stub = OptionalStubber.EMPTY.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).isEmpty());
        }
    }

    @Nested
    class PresentIfPossibleTest {

        @BeforeEach
        void setUp() {
            when(rootStubber.tryToStub(any(Type.class), any()))
                    .thenAnswer(invocation -> {
                        Type type = Types.getRawType(invocation.getArgument(0)).orElseThrow(AssertionError::new);
                        if (type == String.class) {
                            return Result.success("Test");
                        } else if (type == Integer.class) {
                            return Result.success(1337);
                        } else if (type == Double.class) {
                            return Result.failure();
                        } else {
                            throw new AssertionError();
                        }
                    });
        }

        @Test
        void shouldAcceptOptionalWithoutTypeArguments() {
            boolean accepts = OptionalStubber.PRESENT_IF_POSSIBLE.accepts(context, Optional.class);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithoutTypeArguments() {
            Object stub = OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, Optional.class);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).isEmpty());
        }

        @Test
        void shouldAcceptOptionalWithTypeArguments() {
            Type type = new TypeLiteral<Optional<Float>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT_IF_POSSIBLE.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithTypeArgument() {
            Type type = new TypeLiteral<Optional<Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @SuppressWarnings("unchecked")
        @Test
        void givenStubbingFailsItShouldReturnEmpty() {
            Type type = new TypeLiteral<Optional<Double>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).isEmpty());
        }

        @Test
        void shouldAcceptOptionalWithUpperBoundedTypeArguments() {
            Type type = new TypeLiteral<Optional<? extends Integer>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT_IF_POSSIBLE.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithUpperBoundedTypeArgument() {
            Type type = new TypeLiteral<Optional<? extends Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @Test
        void shouldAcceptOptionalWithLowerBoundedTypeArguments() {
            Type type = new TypeLiteral<Optional<? super Integer>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT_IF_POSSIBLE.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithLowerBoundedTypeArgument() {
            Type type = new TypeLiteral<Optional<? super Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @Test
        void shouldStubWithParameterizedTypeStubbingSite() {
            Type type = new TypeLiteral<Optional<Integer>>() {
            }.getType();

            OptionalStubber.PRESENT_IF_POSSIBLE.stub(context, type);

            verify(rootStubber)
                    .tryToStub((Type) Integer.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 0));
        }
    }

    @Nested
    class PresentTest {

        @BeforeEach
        void setUp() {
            when(rootStubber.stub(any(Type.class), any()))
                    .thenAnswer(invocation -> {
                        Type type = Types.getRawType(invocation.getArgument(0)).orElseThrow(AssertionError::new);
                        if (type == String.class) {
                            return "Test";
                        } else if (type == Integer.class) {
                            return 1337;
                        } else {
                            throw new AssertionError();
                        }
                    });
        }

        @Test
        void shouldAcceptNotOptionalWithoutTypeArguments() {
            boolean accepts = OptionalStubber.PRESENT.accepts(context, Optional.class);

            assertThat(accepts)
                    .isFalse();
        }

        @Test
        void shouldAcceptOptionalWithTypeArguments() {
            Type type = new TypeLiteral<Optional<Float>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithTypeArgument() {
            Type type = new TypeLiteral<Optional<Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @Test
        void shouldAcceptOptionalWithUpperBoundedTypeArguments() {
            Type type = new TypeLiteral<Optional<? extends Integer>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithUpperBoundedTypeArgument() {
            Type type = new TypeLiteral<Optional<? extends Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @Test
        void shouldAcceptOptionalWithLowerBoundedTypeArguments() {
            Type type = new TypeLiteral<Optional<? super Integer>>() {
            }.getType();

            boolean accepts = OptionalStubber.PRESENT.accepts(context, type);

            assertThat(accepts)
                    .isTrue();
        }

        @SuppressWarnings("unchecked")
        @Test
        void shouldStubOptionalWithLowerBoundedTypeArgument() {
            Type type = new TypeLiteral<Optional<? super Integer>>() {
            }.getType();

            Object stub = OptionalStubber.PRESENT.stub(context, type);

            assertThat(stub)
                    .isInstanceOfSatisfying(Optional.class, optional -> assertThat(optional).hasValue(1337));
        }

        @Test
        void shouldStubWithParameterizedTypeStubbingSite() {
            Type type = new TypeLiteral<Optional<Integer>>() {
            }.getType();

            OptionalStubber.PRESENT.stub(context, type);

            verify(rootStubber)
                    .stub((Type) Integer.class, StubbingSites.parameterizedType(StubbingSites.unknown(), (ParameterizedType) type, 0));
        }
    }

}