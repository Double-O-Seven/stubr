package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.RootStubber;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.strategy.StubbingStrategies.rootStubber;
import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.api.Assertions.assertThat;

class RootStubberStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testRootStubberStubber() {
        return stubbingStrategyTester()
                .accepts(RootStubber.class)
                .andStubSatisfies(value -> assertThat(value).isInstanceOf(RootStubber.class))
                .rejects(Object.class)
                .rejects(String.class)
                .test(rootStubber());
    }

}