package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

final class SuppliedValueStubbingStrategy extends SimpleStubbingStrategy<Object> {

    private final Type valueType;
    private final StubValueSupplier<?> valueSupplier;
    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    SuppliedValueStubbingStrategy(Type valueType, StubValueSupplier<?> valueSupplier) {
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
        return getNextValue(context);
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return getNextValue(context);
    }

    private Object getNextValue(StubbingContext context) {
        return valueSupplier.get(context, sequenceNumber.getAndIncrement());
    }

}
