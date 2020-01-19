package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;

import java.lang.reflect.ParameterizedType;

final class EnumValueStubber extends SimpleStubber<Object> {

    static final EnumValueStubber INSTANCE = new EnumValueStubber();

    private EnumValueStubber() {
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type.isEnum() && type.getEnumConstants().length > 0;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return false;
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return type.getEnumConstants()[0];
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        throw new StubbingException(context.getSite(), type);
    }

}
