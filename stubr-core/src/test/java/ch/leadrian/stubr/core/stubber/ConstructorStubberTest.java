package ch.leadrian.stubr.core.stubber;

import ch.leadrian.equalizer.Equals;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.stubbingsite.ConstructorParameterStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static ch.leadrian.equalizer.Equalizer.equalsBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConstructorStubberTest {

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
    void givenUnambiguousPublicConstructorItShouldAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 2);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousPublicConstructorItShouldReturnStub() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 2);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(new Foo("Test", 1337));
    }

    @Test
    void givenUnambiguousProtectedConstructorItShouldAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 1);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousProtectedConstructorItShouldReturnStub() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 1);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(new Foo("Test", 0));
    }

    @Test
    void givenUnambiguousPackagePrivateConstructorItShouldAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 0);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isTrue();
    }

    @Test
    void givenUnambiguousPackagePrivateConstructorItShouldReturnStub() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> constructor.getParameterCount() == 0);

        Object stub = stubber.stub(context, Foo.class);

        assertThat(stub)
                .isEqualTo(new Foo("", 0));
    }

    @Test
    void shouldStubAtConstructorParameterSite() throws Exception {
        Constructor constructor = Foo.class.getConstructor(String.class, int.class);
        Parameter stringParameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite stringSite = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, stringParameter);
        Parameter intParameter = constructor.getParameters()[1];
        ConstructorParameterStubbingSite intSite = StubbingSites.constructorParameter(StubbingSites.unknown(), constructor, intParameter);
        ConstructorStubber stubber = new ConstructorStubber(c -> c.getParameterCount() == 2);

        stubber.stub(context, Foo.class);

        verify(rootStubber, atLeastOnce()).stub((Type) String.class, stringSite);
        verify(rootStubber, atLeastOnce()).stub((Type) int.class, intSite);
    }

    @Test
    void givenAmbiguousConstructorItShouldNotAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        boolean accepts = stubber.accepts(context, Foo.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenAmbiguousConstructorItShouldThrowException() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Foo.class));

        assertThat(caughtThrowable)
                .isInstanceOf(StubbingException.class)
                .hasMessage("Cannot stub class ch.leadrian.stubr.core.stubber.ConstructorStubberTest$Foo at UnknownStubbingSite: No matching constructor found");
    }

    @Test
    void givenUnambiguousPrivateConstructorItShouldNotAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        boolean accepts = stubber.accepts(context, Bar.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenUnambiguousPrivateConstructorItShouldThrowException() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Bar.class));

        assertThat(caughtThrowable)
                .isInstanceOf(StubbingException.class)
                .hasMessage("Cannot stub class ch.leadrian.stubr.core.stubber.ConstructorStubberTest$Bar at UnknownStubbingSite: No matching constructor found");
    }

    @Test
    void givenUnambiguousNonStaticConstructorItShouldNotAccept() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        boolean accepts = stubber.accepts(context, Bar.class);

        assertThat(accepts)
                .isFalse();
    }

    @Test
    void givenUnambiguousNonStaticConstructorItShouldThrowException() {
        ConstructorStubber stubber = new ConstructorStubber(constructor -> true);

        Throwable caughtThrowable = catchThrowable(() -> stubber.stub(context, Bar.class));

        assertThat(caughtThrowable)
                .isInstanceOf(StubbingException.class)
                .hasMessage("Cannot stub class ch.leadrian.stubr.core.stubber.ConstructorStubberTest$Bar at UnknownStubbingSite: No matching constructor found");
    }

    @SuppressWarnings("WeakerAccess")
    static class Foo {

        private static final Equals<Foo> EQUALS = equalsBuilder(Foo.class)
                .compare(foo -> foo.stringValue)
                .compare(foo -> foo.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        public Foo(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        protected Foo(String stringValue) {
            this(stringValue, 0);
        }

        @SuppressWarnings("unused")
        Foo() {
            this("");
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }
    }

    private static class Bar {

        private Bar() {
        }

    }

}