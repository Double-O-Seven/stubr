package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.getRawType;

enum NullValueStubber implements Stubber {
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
