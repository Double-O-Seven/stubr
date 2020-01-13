package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ConstantValueStubber extends SimpleStubber<Object> {

    private final Type valueType;
    private final Object value;

    ConstantValueStubber(Type valueClass, Object value) {
        requireNonNull(valueClass, "valueType");
        requireNonNull(value, "value");
        this.valueType = valueClass;
        this.value = value;
    }

    @Override
    protected boolean accepts(StubbingContext context, Class<?> type) {
        return valueType == type;
    }

    @Override
    protected boolean accepts(StubbingContext context, ParameterizedType type) {
        return valueType.equals(type);
    }

    @Override
    protected Object stub(StubbingContext context, Class<?> type) {
        return value;
    }

    @Override
    protected Object stub(StubbingContext context, ParameterizedType type) {
        return value;
    }
}
