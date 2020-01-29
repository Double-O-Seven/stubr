package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.getRawType;

enum NullValueStubbingStrategy implements StubbingStrategy {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(clazz -> !clazz.isPrimitive())
                .isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return null;
    }
}
