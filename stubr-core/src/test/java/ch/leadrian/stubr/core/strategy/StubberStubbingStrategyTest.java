package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Stubber;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.strategy.StubbingStrategies.stubber;
import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.api.Assertions.assertThat;

class StubberStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testStubberStubber() {
        return stubbingStrategyTester()
                .accepts(Stubber.class)
                .andStubSatisfies(value -> assertThat(value).isInstanceOf(Stubber.class))
                .rejects(Object.class)
                .rejects(String.class)
                .test(stubber());
    }

}