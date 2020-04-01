package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CompositeSelectorTest {

    @Test
    void shouldReturnFirstNonEmptyValue() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector1 = (c, values) -> Optional.of(values.get(0));
        Selector<String> selector2 = (c, values) -> Optional.of(values.get(1));
        Selector<String> selector3 = (c, values) -> Optional.of(values.get(2));
        Selector<String> compositeSelector = Selectors.compose(selector1, selector2, selector3);

        Optional<String> value = compositeSelector.select(context, asList("ABC", "123", "XYZ"));

        assertThat(value)
                .hasValue("ABC");
    }

    @Test
    void shouldSkipEmptySelections() {
        StubbingContext context = mock(StubbingContext.class);
        Selector<String> selector1 = (c, values) -> Optional.empty();
        Selector<String> selector2 = (c, values) -> Optional.empty();
        Selector<String> selector3 = (c, values) -> Optional.of(values.get(2));
        Selector<String> compositeSelector = Selectors.compose(selector1, selector2, selector3);

        Optional<String> value = compositeSelector.select(context, asList("ABC", "123", "XYZ"));

        assertThat(value)
                .hasValue("XYZ");
    }

}