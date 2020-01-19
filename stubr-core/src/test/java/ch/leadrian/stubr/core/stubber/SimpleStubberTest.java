package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.testing.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleStubberTest {

    @TestFactory
    <T> Stream<DynamicTest> testSimpleStubber() {
        return stubberTester()
                .accepts(String.class)
                .andStubs("ABC")
                .accepts(new TypeLiteral<List<String>>() {
                })
                .andStubs(singletonList("DEF"))
                .accepts(new ParameterizedTypeLiteral<List<? extends String>>() {
                }.getActualTypeArgument(0))
                .andStubs("ABC")
                .accepts(new ParameterizedTypeLiteral<List<? super String>>() {
                }.getActualTypeArgument(0))
                .andStubs("ABC")
                .accepts(new ParameterizedTypeLiteral<List<? extends List<String>>>() {
                }.getActualTypeArgument(0))
                .andStubs(singletonList("DEF"))
                .accepts(new ParameterizedTypeLiteral<List<? super List<String>>>() {
                }.getActualTypeArgument(0))
                .andStubs(singletonList("DEF"))
                .rejects(Object.class)
                .rejects(new TypeLiteral<List<Object>>() {
                })
                .rejects(new TypeLiteral<T>() {
                })
                .rejects(new TypeLiteral<T[]>() {
                })
                .rejects(new ParameterizedTypeLiteral<List<?>>() {
                }.getActualTypeArgument(0))
                .rejects(new ParameterizedTypeLiteral<List<? extends T>>() {
                }.getActualTypeArgument(0))
                .rejects(new ParameterizedTypeLiteral<List<? super T>>() {
                }.getActualTypeArgument(0))
                .test(new SimpleStubber<Object>() {

                    @Override
                    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
                        return type == String.class;
                    }

                    @Override
                    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
                        return new TypeLiteral<List<String>>() {
                        }.getType().equals(type);
                    }

                    @Override
                    protected Object stubClass(StubbingContext context, Class<?> type) {
                        return "ABC";
                    }

                    @Override
                    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
                        return singletonList("DEF");
                    }
                });
    }

    @Test
    <T> void givenTypeVariableStubShouldThrowException() {
        StubbingSite site = mock(StubbingSite.class);
        StubbingContext context = mock(StubbingContext.class);
        when(context.getSite())
                .thenReturn(site);
        Type type = new TypeLiteral<T>() {
        }.getType();
        Stubber stubber = new SimpleStubber<Object>() {
            @Override
            protected boolean acceptsClass(StubbingContext context, Class<?> type) {
                return false;
            }

            @Override
            protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
                return false;
            }

            @Override
            protected Object stubClass(StubbingContext context, Class<?> type) {
                return null;
            }

            @Override
            protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
                return null;
            }
        };

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOfSatisfying(StubbingException.class, exception -> assertAll(
                        () -> assertThat(exception.getSite()).hasValue(site),
                        () -> assertThat(exception.getType()).hasValue(type)
                ));
    }

    @Test
    <T> void givenGenericTypeArrayStubShouldThrowException() {
        StubbingSite site = mock(StubbingSite.class);
        StubbingContext context = mock(StubbingContext.class);
        when(context.getSite())
                .thenReturn(site);
        Type type = new TypeLiteral<T[]>() {
        }.getType();
        Stubber stubber = new SimpleStubber<Object>() {
            @Override
            protected boolean acceptsClass(StubbingContext context, Class<?> type) {
                return false;
            }

            @Override
            protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
                return false;
            }

            @Override
            protected Object stubClass(StubbingContext context, Class<?> type) {
                return null;
            }

            @Override
            protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
                return null;
            }
        };

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, type));

        assertThat(caughtThrowable)
                .isInstanceOfSatisfying(StubbingException.class, exception -> assertAll(
                        () -> assertThat(exception.getSite()).hasValue(site),
                        () -> assertThat(exception.getType()).hasValue(type)
                ));
    }

}