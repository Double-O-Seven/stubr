package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.Objects.requireNonNull;

final class RandomSelector<T> implements Selector<T> {

    private final Random random;

    RandomSelector(Random random) {
        requireNonNull(random, "random");
        this.random = random;
    }

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        if (values.isEmpty()) {
            return Optional.empty();
        }
        int index = random.nextInt(values.size());
        return Optional.ofNullable(values.get(index));
    }

}
