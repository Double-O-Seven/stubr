package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class FirstElementSelector<T> implements Selector<T> {

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .map(value -> (T) value)
                .findFirst();
    }

}
