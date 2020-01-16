package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.StubberTester;
import ch.leadrian.stubr.core.TestStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
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
                .acceptsAndStubs(List.class, new ArrayList<>())
                .acceptsAndStubs(new TypeLiteral<List<String>>() {
                }, new ArrayList<>())
                .acceptsAndStubs(new TypeLiteral<List<? super String>>() {
                }, new ArrayList<>())
                .acceptsAndStubs(new TypeLiteral<List<?>>() {
                }, new ArrayList<>())
                .acceptsAndStubs(new TypeLiteral<List<? extends String>>() {
                }, new ArrayList<>())
                .rejects(Collection.class)
                .rejects(new TypeLiteral<Collection<String>>() {
                })
                .rejects(new TypeLiteral<Collection<? super String>>() {
                })
                .rejects(new TypeLiteral<Collection<? extends String>>() {
                })
                .rejects(ArrayList.class)
                .rejects(new TypeLiteral<ArrayList<String>>() {
                })
                .rejects(new TypeLiteral<ArrayList<? super String>>() {
                })
                .rejects(new TypeLiteral<ArrayList<? extends String>>() {
                });
        return Stream.of(
                tester.test(Stubbers.collection(List.class, ArrayList::new, context -> 0)),
                tester.test(Stubbers.collection(List.class, ArrayList::new, 0)),
                tester.test(Stubbers.collection(List.class, ArrayList::new))
        ).flatMap(identity());
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyCollectionStubber() {
        ParameterizedTypeLiteral<List<String>> listOfStrings = new ParameterizedTypeLiteral<List<String>>() {
        };
        StubberTester tester = new StubberTester()
                .provideStub(String.class, "foo", "bar", "baz")
                .rejects(List.class)
                .acceptsAndStubs(
                        listOfStrings,
                        newArrayList("foo", "bar", "baz"),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0)
                )
                .rejects(Collection.class)
                .rejects(new TypeLiteral<Collection<String>>() {
                })
                .rejects(new TypeLiteral<Collection<? super String>>() {
                })
                .rejects(new TypeLiteral<Collection<? extends String>>() {
                })
                .rejects(ArrayList.class)
                .rejects(new TypeLiteral<ArrayList<String>>() {
                })
                .rejects(new TypeLiteral<ArrayList<? super String>>() {
                })
                .rejects(new TypeLiteral<ArrayList<? extends String>>() {
                });
        return Stream.of(
                tester.test(Stubbers.collection(List.class, ArrayList::new, context -> 3)),
                tester.test(Stubbers.collection(List.class, ArrayList::new, 3))
        ).flatMap(identity());
    }

    @TestFactory
    Stream<DynamicTest> testUnsupportedParameterization() {
        return new StubberTester()
                .rejects(new TypeLiteral<WeirdList<String, Integer>>() {
                })
                .test(Stubbers.collection(WeirdList.class, values -> new WeirdList(), context -> 3));
    }

    @SuppressWarnings("unused")
    private static final class WeirdList<T, U> extends ArrayList<T> {
    }

}