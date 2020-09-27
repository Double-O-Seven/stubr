/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
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

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoizingStubbingStrategyTest {

    @Test
    void shouldReturnMemoizedValuesSeparatelyForDifferentTypes() {
        StubbingContext context = mock(StubbingContext.class);
        StubbingStrategy strategy = StubbingStrategies.memoized(new TestStubbingStrategy(1337));

        List<Object> intValues = IntStream.iterate(0, i -> i + 1)
                .limit(3)
                .mapToObj(i -> strategy.stub(context, Integer.class))
                .collect(toList());
        List<Object> stringValues = IntStream.iterate(0, i -> i + 1)
                .limit(3)
                .mapToObj(i -> strategy.stub(context, String.class))
                .collect(toList());

        assertAll(
                () -> assertThat(intValues).hasSize(3).containsOnly(1337),
                () -> assertThat(stringValues).hasSize(3).containsOnly("1338")
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldDelegateAccepts(boolean expectedAccepts) {
        StubbingContext context = mock(StubbingContext.class);
        StubbingStrategy delegate = mock(StubbingStrategy.class);
        when(delegate.accepts(context, String.class))
                .thenReturn(expectedAccepts);
        StubbingStrategy strategy = StubbingStrategies.memoized(delegate);

        boolean accepts = strategy.accepts(context, String.class);

        assertThat(accepts)
                .isEqualTo(expectedAccepts);
    }

    private static class TestStubbingStrategy implements StubbingStrategy {

        private int counter;

        TestStubbingStrategy(int counter) {
            this.counter = counter;
        }

        @Override
        public boolean accepts(StubbingContext context, Type type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object stub(StubbingContext context, Type type) {
            if (type == Integer.class) {
                return counter++;
            } else if (type == String.class) {
                return String.valueOf(counter++);
            }
            throw new UnsupportedOperationException();
        }

    }

}