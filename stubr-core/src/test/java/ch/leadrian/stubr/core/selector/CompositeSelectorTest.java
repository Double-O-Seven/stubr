/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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