package ch.leadrian.stubr.core.strategy;

import ch.leadrian.equalizer.Equals;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.testing.TestStubbingSite;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.equalizer.Equalizer.equalsBuilder;
import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;

class FactoryMethodStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testFactoryMethodStubberWithFilter() throws Exception {
        return stubberTester()
                .provideStub("test")
                .provideStub(int.class, 1337)
                .accepts(UnambiguousPublicFactoryMethod.class)
                .andStubs(new UnambiguousPublicFactoryMethod("test", 1337))
                .at(
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPublicFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 0),
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPublicFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 1)
                )
                .accepts(UnambiguousProtectedFactoryMethod.class)
                .andStubs(new UnambiguousProtectedFactoryMethod("test", 1337))
                .at(
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 0),
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousProtectedFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 1)
                )
                .accepts(UnambiguousPackagePrivateFactoryMethod.class)
                .andStubs(new UnambiguousPackagePrivateFactoryMethod("test", 1337))
                .at(
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 0),
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPackagePrivateFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 1)
                )
                .rejects(UnambiguousPrivateFactoryMethod.class)
                .rejects(AmbiguousPublicFactoryMethod.class)
                .rejects(AmbiguousProtectedFactoryMethod.class)
                .rejects(AmbiguousPackagePrivateFactoryMethod.class)
                .rejects(UnambiguousPublicNonStaticMethod.class)
                .rejects(UnambiguousProtectedNonStaticMethod.class)
                .rejects(UnambiguousPackagePrivateNonStaticMethod.class)
                .rejects(NotMatchingFactoryMethod.class)
                .rejects(Foo.class)
                .rejects(Bar.class)
                .rejects(int.class)
                .test(StubbingStrategies.factoryMethod((context, factoryMethod) -> factoryMethod.getParameterCount() == 2));
    }

    @TestFactory
    Stream<DynamicTest> testFactoryMethodStubberAcceptingAnyConstructor() throws Throwable {
        return stubberTester()
                .provideStub("test")
                .provideStub(int.class, 1337)
                .accepts(UnambiguousPublicFactoryMethod.class)
                .andStubs(new UnambiguousPublicFactoryMethod("test", 1337))
                .at(
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPublicFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 0),
                        StubbingSites.methodParameter(TestStubbingSite.INSTANCE, UnambiguousPublicFactoryMethod.class.getDeclaredMethod("get", String.class, int.class), 1)
                )
                .rejects(MultiplePublicFactoryMethods.class)
                .test(StubbingStrategies.factoryMethod());
    }

    @SuppressWarnings("unused")
    private static class MultiplePublicFactoryMethods {

        public static MultiplePublicFactoryMethods get(String stringValue, int intValue) {
            return null;
        }

        public static MultiplePublicFactoryMethods get(String stringValue, long longValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPublicFactoryMethod {

        private static final Equals<UnambiguousPublicFactoryMethod> EQUALS = equalsBuilder(UnambiguousPublicFactoryMethod.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        private UnambiguousPublicFactoryMethod(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        public static UnambiguousPublicFactoryMethod get(String stringValue, int intValue) {
            return new UnambiguousPublicFactoryMethod(stringValue, intValue);
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousProtectedFactoryMethod {

        private static final Equals<UnambiguousProtectedFactoryMethod> EQUALS = equalsBuilder(UnambiguousProtectedFactoryMethod.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        private UnambiguousProtectedFactoryMethod(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        protected static UnambiguousProtectedFactoryMethod get(String stringValue, int intValue) {
            return new UnambiguousProtectedFactoryMethod(stringValue, intValue);
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPackagePrivateFactoryMethod {

        private static final Equals<UnambiguousPackagePrivateFactoryMethod> EQUALS = equalsBuilder(UnambiguousPackagePrivateFactoryMethod.class)
                .compare(value -> value.stringValue)
                .compare(value -> value.intValue)
                .build();

        private final String stringValue;
        private final int intValue;

        private UnambiguousPackagePrivateFactoryMethod(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        static UnambiguousPackagePrivateFactoryMethod get(String stringValue, int intValue) {
            return new UnambiguousPackagePrivateFactoryMethod(stringValue, intValue);
        }

        @Override
        public boolean equals(Object obj) {
            return EQUALS.equals(this, obj);
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPrivateFactoryMethod {

        private static UnambiguousPrivateFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousPublicFactoryMethod {

        public static AmbiguousPublicFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

        public static AmbiguousPublicFactoryMethod get(String stringValue, float intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousProtectedFactoryMethod {

        protected static AmbiguousProtectedFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

        protected static AmbiguousProtectedFactoryMethod get(String stringValue, float intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class AmbiguousPackagePrivateFactoryMethod {

        static AmbiguousPackagePrivateFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

        static AmbiguousPackagePrivateFactoryMethod get(String stringValue, float intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPublicNonStaticMethod {

        public UnambiguousPrivateFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousProtectedNonStaticMethod {

        protected UnambiguousPrivateFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class UnambiguousPackagePrivateNonStaticMethod {

        UnambiguousPrivateFactoryMethod get(String stringValue, int intValue) {
            return null;
        }

    }

    @SuppressWarnings("unused")
    private static class NotMatchingFactoryMethod {

        public static UnambiguousPrivateFactoryMethod get(String stringValue, int intValue, Object objectValue) {
            return null;
        }

    }

    private enum Foo {
    }

    private interface Bar {
    }

}