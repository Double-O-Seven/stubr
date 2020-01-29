package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;
import java.util.Optional;

import static ch.leadrian.stubr.core.type.Types.getRawType;
import static com.google.common.base.Defaults.defaultValue;
import static com.google.common.primitives.Primitives.unwrap;

enum DefaultValueStubbingStrategy implements StubbingStrategy {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getDefaultValue(type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return getDefaultValue(type).orElseThrow(() -> new StubbingException(context.getSite(), type));
    }

    private Optional<?> getDefaultValue(Type type) {
        return getRawType(type).flatMap(this::getDefaultValue);
    }

    private Optional<?> getDefaultValue(Class<?> clazz) {
        return Optional.ofNullable(defaultValue(unwrap(clazz)));
    }
}
