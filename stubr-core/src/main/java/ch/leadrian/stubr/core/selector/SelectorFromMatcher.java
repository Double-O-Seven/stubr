package ch.leadrian.stubr.core.selector;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class SelectorFromMatcher<T> implements Selector<T> {

    private final Matcher<? super T> matcher;

    SelectorFromMatcher(Matcher<? super T> matcher) {
        requireNonNull(matcher, "matcher");
        this.matcher = matcher;
    }

    @Override
    public Optional<T> select(StubbingContext context, List<? extends T> values) {
        List<? extends T> matchingValues = values.stream()
                .filter(value -> matcher.matches(context, value))
                .collect(toList());
        return Optional.ofNullable(matchingValues.size() == 1 ? matchingValues.get(0) : null);
    }

}
