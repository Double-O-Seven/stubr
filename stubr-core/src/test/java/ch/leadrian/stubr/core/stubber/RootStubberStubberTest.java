package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.stubber.Stubbers.rootStubber;
import static ch.leadrian.stubr.core.testing.StubberTester.stubberTester;
import static org.assertj.core.api.Assertions.assertThat;

class RootStubberStubberTest {

    @TestFactory
    Stream<DynamicTest> testRootStubberStubber() {
        return stubberTester()
                .accepts(RootStubber.class)
                .andStubSatisfies(value -> assertThat(value).isInstanceOf(RootStubber.class))
                .rejects(Object.class)
                .rejects(String.class)
                .test(rootStubber());
    }

}