package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.testing.TestStubbingSite;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Collections.singletonList;

class ImplementationStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testImplementationStubberWithNonParameterizedClass() {
        return stubbingStrategyTester()
                .provideStub("Test")
                .accepts(CharSequence.class)
                .andStubs("Test")
                .at(TestStubbingSite.INSTANCE)
                .rejects(String.class)
                .rejects(Object.class)
                .test(
                        StubbingStrategies.implementation(CharSequence.class, String.class),
                        StubbingStrategies.implementation(new TypeLiteral<CharSequence>() {}, new TypeLiteral<String>() {}),
                        StubbingStrategies.implementation(CharSequence.class, new TypeLiteral<String>() {}),
                        StubbingStrategies.implementation(new TypeLiteral<CharSequence>() {}, String.class)
                );
    }

    @SuppressWarnings("unchecked")
    @TestFactory
    Stream<DynamicTest> testImplementationStubberWithParameterizedClass() {
        return stubbingStrategyTester()
                .provideStub(new TypeLiteral<List<String>>() {}, singletonList("Test"))
                .accepts(new TypeLiteral<Collection<? extends CharSequence>>() {})
                .andStubs(singletonList("Test"))
                .at(TestStubbingSite.INSTANCE)
                .rejects(new TypeLiteral<Collection<CharSequence>>() {})
                .rejects(new TypeLiteral<Collection<String>>() {})
                .rejects(new TypeLiteral<List<String>>() {})
                .rejects(String.class)
                .rejects(Object.class)
                .test(StubbingStrategies.implementation(new TypeLiteral<Collection<? extends CharSequence>>() {}, new TypeLiteral<List<String>>() {}));
    }

}