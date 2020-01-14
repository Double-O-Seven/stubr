package ch.leadrian.stubr.core.stubber;

import ch.leadrian.equalizer.Equals;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubbingsite.MethodParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsBuilder;
import static ch.leadrian.stubr.core.stubber.FactoryMethodStubberTest.Foo.foo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FactoryMethodStubberTest {

    private StubbingContext context;
    private RootStubber rootStubber;

    @BeforeEach
    void setUp() {
        rootStubber = mock(RootStubber.class);
        when(rootStubber.stub(any(Type.class), any()))
                .thenAnswer(invocation -> {
                    Class<?> rawType = invocation.getArgument(0);
                    if (rawType == String.class) {
                        return "Test";
                    } else if (rawType == int.class) {
                        return 1337;
                    } else {
                        throw new AssertionError();
                    }
                });
        context = new StubbingContext(rootStubber, StubbingSites.unknown());
    }

    @Test
    void givenUnambiguousPublicMethodItShouldAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 2);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousPublicMethodItShouldReturnStub() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 2);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(foo("Test", 1337));
    }

    @Test
    void givenUnambiguousProtectedMethodItShouldAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 1);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousProtectedMethodItShouldReturnStub() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 1);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(foo("Test", 0));
    }

    @Test
    void givenUnambiguousPackagePrivateMethodItShouldAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 0);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousPackagePrivateMethodItShouldReturnStub() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 0);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(foo("", 0));
    }

    @Test
    void shouldStubAtMethodParameterSite() throws Exception {
        Method factoryMethod = Foo.class.getMethod("foo", String.class, int.class);
        Parameter stringParameter = factoryMethod.getParameters()[0];
        MethodParameterStubbingSite stringSite = StubbingSites.methodParameter(StubbingSites.unknown(), factoryMethod, stringParameter);
        Parameter intParameter = factoryMethod.getParameters()[1];
        MethodParameterStubbingSite intSite = StubbingSites.methodParameter(StubbingSites.unknown(), factoryMethod, intParameter);
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> method.getParameterCount() == 2);

        stubber.stub(context, Foo.class);

        verify(rootStubber, atLeastOnce()).stub((Type) String.class, stringSite);
        verify(rootStubber, atLeastOnce()).stub((Type) int.class, intSite);
    }

    @Test
    void givenAmbiguousMethodItShouldNotAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenAmbiguousMethodItShouldThrowException() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Foo.class));

        assertThat(caughtThrowable)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void givenUnambiguousPrivateMethodItShouldNotAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        boolean accepts = stubber.accepts(context, Bar.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenUnambiguousPrivateMethodItShouldThrowException() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Bar.class));

        assertThat(caughtThrowable)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void givenUnambiguousNonStaticMethodItShouldNotAccept() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        boolean accepts = stubber.accepts(context, Bar.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenUnambiguousNonStaticMethodItShouldThrowException() {
        FactoryMethodStubber stubber = new FactoryMethodStubber(method -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Bar.class));

        assertThat(caughtThrowable)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    static class Foo {

        private static final Equals<Foo> EQUALS = equalsBuilder(Foo.class)
                .compare(foo -> foo.stringValue)
                .compare(foo -> foo.intValue)
                .build();

        private String stringValue;
        private int intValue;

        public static Foo foo(String stringValue, int intValue) {
            Foo foo = new Foo();
            foo.stringValue = stringValue;
            foo.intValue = intValue;
            return foo;
        }

        protected static Foo foo(String stringValue) {
            return foo(stringValue, 0);
        }

        @SuppressWarnings("unused")
        static Foo foo() {
            return foo("");
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }
    }

    @SuppressWarnings("unused")
    private static class Bar {

        private static Bar bar() {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class Qux {

        Qux qux() {
            return null;
        }

    }

}