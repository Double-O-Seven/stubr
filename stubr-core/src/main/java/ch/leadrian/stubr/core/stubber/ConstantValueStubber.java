package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.util.Types.getActualClass;
import static java.util.Objects.requireNonNull;

final class ConstantValueStubber implements Stubber {

    private final Class<?> valueClass;
    private final Object value;

    ConstantValueStubber(Class<?> valueClass, Object value) {
        requireNonNull(valueClass, "valueClass");
        requireNonNull(value, "value");
        this.valueClass = valueClass;
        this.value = value;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getActualClass(type)
                .filter(valueClass::equals)
                .isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return value;
    }
}
