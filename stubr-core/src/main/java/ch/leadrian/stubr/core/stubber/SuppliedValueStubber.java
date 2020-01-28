package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import static java.util.Objects.requireNonNull;

final class SuppliedValueStubber extends SimpleStubber<Object> {

    private final Type valueType;
    private final IntFunction<?> valueSupplier;
    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    SuppliedValueStubber(Type valueType, IntFunction<?> valueSupplier) {
        requireNonNull(valueType, "valueType");
        requireNonNull(valueSupplier, "valueSupplier");
        this.valueType = valueType;
        this.valueSupplier = valueSupplier;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return valueType == type;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return valueType.equals(type);
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return getNextValue();
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return getNextValue();
    }

    private Object getNextValue() {
        return valueSupplier.apply(sequenceNumber.getAndIncrement());
    }

}
