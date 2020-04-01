package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FirstElementSelectorTest {

    @Test
    void givenNoValuesItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.first();

        Optional<String> value = selector.select(context, emptyList());

        assertThat(value)
                .isEmpty();
    }

    @Test
    void givenOnlyNullValuesItShouldReturnEmpty() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.first();

        Optional<String> value = selector.select(context, asList(null, null, null));

        assertThat(value)
                .isEmpty();
    }

    @Test
    void shouldReturnFirstNonNullElement() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector = Selectors.first();

        Optional<String> value = selector.select(context, asList(null, "ABC", "DEF"));

        assertThat(value)
                .hasValue("ABC");
    }

}