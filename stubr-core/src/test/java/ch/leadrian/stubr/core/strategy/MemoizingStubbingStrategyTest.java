package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
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
        StubbingContext context = new StubbingContext(mock(Stubber.class), mock(StubbingSite.class));
        StubbingStrategy strategy = StubbingStrategies.memoizing(new TestStubbingStrategy(1337));

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
        StubbingContext context = new StubbingContext(mock(Stubber.class), mock(StubbingSite.class));
        StubbingStrategy delegate = mock(StubbingStrategy.class);
        when(delegate.accepts(context, String.class))
                .thenReturn(expectedAccepts);
        StubbingStrategy strategy = StubbingStrategies.memoizing(delegate);

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