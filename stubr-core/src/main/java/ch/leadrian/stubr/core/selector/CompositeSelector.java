package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class CompositeSelector<T> implements Selector<T> {

    private final List<Selector<T>> selectors;

    CompositeSelector(List<Selector<T>> selectors) {
        requireNonNull(selectors, "selectors");
        this.selectors = ImmutableList.copyOf(selectors);
    }

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        return selectors.stream().map(selector -> selector.select(context, values))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
