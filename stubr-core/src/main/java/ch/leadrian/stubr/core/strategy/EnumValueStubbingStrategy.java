package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Selector;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class EnumValueStubbingStrategy extends SimpleStubbingStrategy<Object> {

    static final EnumValueStubbingStrategy FIRST_ELEMENT = new EnumValueStubbingStrategy(FirstElementSelector.INSTANCE);

    private final Selector<Enum<?>> selector;

    EnumValueStubbingStrategy(Selector<Enum<?>> selector) {
        requireNonNull(selector, "selector");
        this.selector = selector;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type.isEnum() && selectValue(context, type).isPresent();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return false;
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return selectValue(context, type).orElseThrow(() -> new StubbingException(context.getSite(), type));
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        throw new StubbingException(context.getSite(), type);
    }

    private Optional<? extends Enum<?>> selectValue(StubbingContext context, Class<?> type) {
        List<Enum<?>> values = stream(type.getEnumConstants())
                .map(value -> (Enum<?>) value)
                .collect(toList());
        return selector.select(context, values);
    }

    private enum FirstElementSelector implements Selector<Enum<?>> {
        INSTANCE;

        @Override
        public Optional<Enum<?>> select(StubbingContext context, List<? extends Enum<?>> values) {
            return Optional.ofNullable(values.isEmpty() ? null : values.get(0));
        }
    }

}
