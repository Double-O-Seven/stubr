package ch.leadrian.stubr.core.stubber;

import ch.leadrian.equalizer.Equals;
import ch.leadrian.stubr.core.TestStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsBuilder;
import static ch.leadrian.stubr.core.StubberTester.stubberTester;

class ConstructorStubberTest {

    @TestFactory
    Stream<DynamicTest> testConstructorStubber() throws Exception {
        return stubberTester()
                .provideStub("test")
                .provideStub(int.class, 1337)
                .accepts(UnambiguousPublicConstructor.class)
                .andStubs(new UnambiguousPublicConstructor("test", 1337))
                .atSite(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPublicConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .accepts(UnambiguousProtectedConstructor.class)
                .andStubs(new UnambiguousProtectedConstructor("test", 1337))
                .atSite(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .accepts(UnambiguousPackagePrivateConstructor.class)
                .andStubs(new UnambiguousPackagePrivateConstructor("test", 1337))
                .atSite(
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateConstructor.class.getDeclaredConstructor(String.class, int.class), 0),
                        StubbingSites.constructorParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateConstructor.class.getDeclaredConstructor(String.class, int.class), 1)
                )
                .rejects(UnambiguousPrivateConstructor.class)
                .rejects(AmbiguousPublicConstructor.class)
                .rejects(AmbiguousProtectedConstructor.class)
                .rejects(AmbiguousPackagePrivateConstructor.class)
                .test(Stubbers.constructor(constructor -> constructor.getParameterCount() == 2));
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

        public UnambiguousPublicConstructor(String stringValue) {
            this(stringValue, 0);
        }

        public UnambiguousPublicConstructor() {
            this("", 0);
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

}