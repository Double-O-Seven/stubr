package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.StubbingSites;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

class ProxyStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testNonCachingProxyStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 2, 3),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(StubbingStrategies.proxy(false));
    }

    @TestFactory
    Stream<DynamicTest> testCachingProxyStubber() {
        return stubbingStrategyTester()
                .provideStub(int.class, 1, 2, 3)
                .provideStub("Test")
                .accepts(Foo.class)
                .andStubSatisfies(stub -> assertThat(stub).isInstanceOfSatisfying(Foo.class, foo -> {
                    assertAll(
                            () -> assertThat(asList(foo.getInt(), foo.getInt(), foo.getInt())).containsExactly(1, 1, 1),
                            () -> assertThat(foo.getAnotherInt()).isEqualTo(1234),
                            () -> assertThat(foo.getString()).isEqualTo("Test")
                    );
                }))
                .rejects(Bar.class)
                .rejects(Qux.class)
                .test(
                        StubbingStrategies.proxy(true),
                        StubbingStrategies.proxy()
                );
    }

    @Test
    void testProxyEquals() {
        StubbingContext context = new StubbingContext(mock(Stubber.class), StubbingSites.unknown());
        StubbingStrategy stubbingStrategy = StubbingStrategies.proxy();

        Foo foo1 = (Foo) stubbingStrategy.stub(context, Foo.class);
        Foo foo2 = (Foo) stubbingStrategy.stub(context, Foo.class);

        new EqualsTester()
                .addEqualityGroup(foo1, foo1)
                .addEqualityGroup(foo2, foo2)
                .addEqualityGroup("Test")
                .testEquals();
    }

    @Test
    void toStringShouldNotReturnStubbedString() {
        StubbingContext context = new StubbingContext(mock(Stubber.class), StubbingSites.unknown());
        StubbingStrategy stubbingStrategy = StubbingStrategies.proxy();

        Foo foo = (Foo) stubbingStrategy.stub(context, Foo.class);

        assertThat(foo.toString())
                .startsWith("Stubbed ch.leadrian.stubr.core.strategy.ProxyStubbingStrategyTest$Foo");
    }

    private interface Foo {

        int getInt();

        default int getAnotherInt() {
            return 1234;
        }

        String getString();

    }

    private abstract class Bar {
    }

    private class Qux {
    }

}