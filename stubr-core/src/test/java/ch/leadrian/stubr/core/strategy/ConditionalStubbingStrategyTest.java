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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConditionalStubbingStrategyTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "true, false, false",
            "false, true, false",
            "false, false, false"
    })
    void shouldAcceptIfAndOnlyIfDelegateAcceptsAndMatcherMatches(boolean delegateMatch, boolean matcherMatch, boolean expectedResult) {
        StubbingContext context = mock(StubbingContext.class);
        Type type = String.class;
        StubbingStrategy delegate = mock(StubbingStrategy.class);
        when(delegate.accepts(context, type))
                .thenReturn(delegateMatch);
        Matcher<? super Type> matcher = (c, t) -> matcherMatch;
        ConditionalStubbingStrategy strategy = new ConditionalStubbingStrategy(delegate, matcher);

        boolean result = strategy.accepts(context, type);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnStubbedValueFromDelegate() {
        StubbingContext context = mock(StubbingContext.class);
        Type type = String.class;
        StubbingStrategy delegate = mock(StubbingStrategy.class);
        when(delegate.stub(context, type))
                .thenReturn("Test");
        ConditionalStubbingStrategy strategy = new ConditionalStubbingStrategy(delegate, (c, t) -> true);

        Object stub = strategy.stub(context, type);

        assertThat(stub)
                .isEqualTo("Test");
    }

}