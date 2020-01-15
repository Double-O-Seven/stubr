package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubberTester;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static org.assertj.core.util.Lists.newArrayList;

class CollectionStubberTest {

    @TestFactory
    Stream<DynamicTest> testEmptyCollectionStubber() {
        StubberTester tester = new StubberTester()
                .accepts(List.class, new ArrayList<>())
                .accepts(new TypeLiteral<List<String>>() {
                }, new ArrayList<>())
                .accepts(new TypeLiteral<List<? super String>>() {
                }, new ArrayList<>())
                .accepts(new TypeLiteral<List<?>>() {
                }, new ArrayList<>())
                .accepts(new TypeLiteral<List<? extends String>>() {
                }, new ArrayList<>())
                .reject(Collection.class)
                .reject(new TypeLiteral<Collection<String>>() {
                })
                .reject(new TypeLiteral<Collection<? super String>>() {
                })
                .reject(new TypeLiteral<Collection<? extends String>>() {
                })
                .reject(ArrayList.class)
                .reject(new TypeLiteral<ArrayList<String>>() {
                })
                .reject(new TypeLiteral<ArrayList<? super String>>() {
                })
                .reject(new TypeLiteral<ArrayList<? extends String>>() {
                });
        return Stream.of(
                tester.test(Stubbers.collection(List.class, ArrayList::new, context -> 0)),
                tester.test(Stubbers.collection(List.class, ArrayList::new, 0)),
                tester.test(Stubbers.collection(List.class, ArrayList::new))
        ).flatMap(identity());
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyCollectionStubber() {
        StubberTester tester = new StubberTester()
                .provideStub(String.class, "foo", "bar", "baz")
                .reject(List.class)
                .accepts(new TypeLiteral<List<String>>() {
                }, newArrayList("foo", "bar", "baz"))
                .reject(Collection.class)
                .reject(new TypeLiteral<Collection<String>>() {
                })
                .reject(new TypeLiteral<Collection<? super String>>() {
                })
                .reject(new TypeLiteral<Collection<? extends String>>() {
                })
                .reject(ArrayList.class)
                .reject(new TypeLiteral<ArrayList<String>>() {
                })
                .reject(new TypeLiteral<ArrayList<? super String>>() {
                })
                .reject(new TypeLiteral<ArrayList<? extends String>>() {
                });
        return Stream.of(
                tester.test(Stubbers.collection(List.class, ArrayList::new, context -> 3)),
                tester.test(Stubbers.collection(List.class, ArrayList::new, 3))
        ).flatMap(identity());
    }

}