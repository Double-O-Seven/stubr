package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SelectorFromMatcherTest {

    @Test
    void givenNoValuesItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.fromMatcher(((c, v) -> true));

        Optional<String> value = selector.select(context, emptyList());

        assertThat(value)
                .isEmpty();
    }

    @Test
    void givenNoMatchItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.fromMatcher(((c, v) -> v.length() == 2));

        Optional<String> value = selector.select(context, asList("a", "b", "c"));

        assertThat(value)
                .isEmpty();
    }

    @Test
    void givenMultipleMatchesItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.fromMatcher(((c, v) -> v.length() == 2));

        Optional<String> value = selector.select(context, asList("a", "bb", "cc", "ddd"));

        assertThat(value)
                .isEmpty();
    }

    @Test
    void givenExactlyOneMatchItShouldReturnMatchingValue() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.fromMatcher(((c, v) -> v.length() == 2));

        Optional<String> value = selector.select(context, asList("a", "bb", "ccc"));

        assertThat(value)
                .hasValue("bb");
    }

}