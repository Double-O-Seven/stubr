package ch.leadrian.stubr.core;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SelectorTest {

    @Nested
    class OrElseTests {

        @Test
        void givenValueIsSelectedItShouldNotUseFallback() {
            StubbingContext context = mock(StubbingContext.class);
            Selector<String> selector1 = (c, values) -> Optional.of(values.get(0));
            Selector<String> selector2 = (c, values) -> Optional.of(values.get(1));
            Selector<String> compositeSelector = selector1.orElse(selector2);

            Optional<String> value = compositeSelector.select(context, asList("ABC", "123"));

            assertThat(value)
                    .hasValue("ABC");
        }

        @Test
        void givenNoValueIsSelectedItShouldUseFallback() {
            StubbingContext context = mock(StubbingContext.class);
            Selector<String> selector1 = (c, values) -> Optional.empty();
            Selector<String> selector2 = (c, values) -> Optional.of(values.get(1));
            Selector<String> compositeSelector = selector1.orElse(selector2);

            Optional<String> value = compositeSelector.select(context, asList("ABC", "123"));

            assertThat(value)
                    .hasValue("123");
        }

    }

}