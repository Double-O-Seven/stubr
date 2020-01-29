package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.site.StubbingSites;
import ch.leadrian.stubr.core.testing.ParameterizedTypeLiteral;
import ch.leadrian.stubr.core.testing.TestStubbingSite;
import ch.leadrian.stubr.core.type.TypeLiteral;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.core.testing.StubbingStrategyTester.stubbingStrategyTester;
import static org.assertj.core.util.Lists.newArrayList;

class CollectionStubbingStrategyTest {

    @TestFactory
    Stream<DynamicTest> testEmptyCollectionStubber() {
        return stubbingStrategyTester()
                .accepts(List.class)
                .andStubs(new ArrayList<>())
                .accepts(new TypeLiteral<List<String>>() {})
                .andStubs(new ArrayList<>())
                .accepts(new TypeLiteral<List<? super String>>() {})
                .andStubs(new ArrayList<>())
                .accepts(new TypeLiteral<List<?>>() {})
                .andStubs(new ArrayList<>())
                .accepts(new TypeLiteral<List<? extends String>>() {})
                .andStubs(new ArrayList<>())
                .rejects(Collection.class)
                .rejects(new TypeLiteral<Collection<String>>() {})
                .rejects(new TypeLiteral<Collection<? super String>>() {})
                .rejects(new TypeLiteral<Collection<? extends String>>() {})
                .rejects(ArrayList.class)
                .rejects(new TypeLiteral<ArrayList<String>>() {})
                .rejects(new TypeLiteral<ArrayList<? super String>>() {})
                .rejects(new TypeLiteral<ArrayList<? extends String>>() {})
                .test(
                        StubbingStrategies.collection(List.class, ArrayList::new, context -> 0),
                        StubbingStrategies.collection(List.class, ArrayList::new, 0),
                        StubbingStrategies.collection(List.class, ArrayList::new)
                );
    }

    @TestFactory
    Stream<DynamicTest> testNonEmptyCollectionStubber() {
        ParameterizedTypeLiteral<List<String>> listOfStrings = new ParameterizedTypeLiteral<List<String>>() {};
        return stubbingStrategyTester()
                .provideStub(String.class, "foo", "bar", "baz")
                .rejects(List.class)
                .accepts(listOfStrings)
                .andStubs(newArrayList("foo", "bar", "baz"))
                .at(
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0),
                        StubbingSites.parameterizedType(TestStubbingSite.INSTANCE, listOfStrings.getType(), 0)
                )
                .rejects(Collection.class)
                .rejects(new TypeLiteral<Collection<String>>() {})
                .rejects(new TypeLiteral<Collection<? super String>>() {})
                .rejects(new TypeLiteral<Collection<? extends String>>() {})
                .rejects(ArrayList.class)
                .rejects(new TypeLiteral<ArrayList<String>>() {})
                .rejects(new TypeLiteral<ArrayList<? super String>>() {})
                .rejects(new TypeLiteral<ArrayList<? extends String>>() {})
                .test(StubbingStrategies.collection(List.class, ArrayList::new, context -> 3), StubbingStrategies.collection(List.class, ArrayList::new, 3));
    }

    @TestFactory
    Stream<DynamicTest> testUnsupportedParameterization() {
        return stubbingStrategyTester()
                .rejects(new TypeLiteral<WeirdList<String, Integer>>() {})
                .test(StubbingStrategies.collection(WeirdList.class, values -> new WeirdList(), context -> 3));
    }

    @SuppressWarnings("unused")
    private static final class WeirdList<T, U> extends ArrayList<T> {
    }

}