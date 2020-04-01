package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RandomSelectorTest {

    @TestFactory
    Stream<DynamicTest> testRandomness() {
        StubbingContext context = mock(StubbingContext.class);
        List<String> values = asList("ABC", "DEF", "GHI");
        return IntStream.iterate(0, i -> i + 1)
                .limit(13)
                .mapToObj(i -> Stream.of(
                        Selectors.random(),
                        Selectors.random(1337L),
                        Selectors.random(new Random(1234L))
                ))
                .flatMap(identity())
                .map(selector -> DynamicTest.dynamicTest("random selector should select element", () -> {
                    Optional<Object> selectedValue = selector.select(context, values);
                    assertThat(selectedValue)
                            .hasValueSatisfying(value -> assertThat(value).isIn(values));
                }));
    }

    @Test
    void givenNoValuesItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.random(new Random(1234L));

        Optional<String> value = selector.select(context, emptyList());

        assertThat(value)
                .isEmpty();
    }

}