package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class ImplementationStubbingStrategy extends SimpleStubbingStrategy<Object> {

    private final Type targetType;
    private final Type implementationType;

    ImplementationStubbingStrategy(Type targetType, Type implementationType) {
        requireNonNull(targetType, "targetType");
        requireNonNull(implementationType, "implementationType");
        this.targetType = targetType;
        this.implementationType = implementationType;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return targetType == type;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return targetType.equals(type);
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return stub(context);
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return stub(context);
    }

    private Object stub(StubbingContext context) {
        return context.getStubber().stub(implementationType, context.getSite());
    }

}
