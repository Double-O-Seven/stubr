package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.stubbingsite.ArrayStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.function.ToIntFunction;

import static java.util.Objects.requireNonNull;

final class ArrayStubber extends SimpleStubber<Object[]> {

    private final ToIntFunction<? super StubbingContext> arraySize;

    ArrayStubber(ToIntFunction<? super StubbingContext> arraySize) {
        requireNonNull(arraySize, "arraySize");
        this.arraySize = arraySize;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type.isArray();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return false;
    }

    @Override
    protected Object[] stubClass(StubbingContext context, Class<?> type) {
        Class<?> componentType = type.getComponentType();
        Object[] array = (Object[]) Array.newInstance(componentType, arraySize.applyAsInt(context));
        ArrayStubbingSite site = StubbingSites.array(context.getSite(), componentType);
        for (int i = 0; i < array.length; i++) {
            array[i] = context.getStubber().stub(componentType, site);
        }
        return array;
    }

    @Override
    protected Object[] stubParameterizedType(StubbingContext context, ParameterizedType type) {
        throw new StubbingException(context.getSite(), type);
    }
}
