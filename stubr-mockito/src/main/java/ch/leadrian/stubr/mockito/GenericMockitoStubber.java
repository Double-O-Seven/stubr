package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubber.SimpleStubber;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

final class GenericMockitoStubber extends SimpleStubber<Object> {

    static final GenericMockitoStubber FINAL_STUBBING_INSTANCE = new GenericMockitoStubber(true);
    static final GenericMockitoStubber OPEN_ONLY_STUBBING_INSTANCE = new GenericMockitoStubber(false);

    private final boolean stubFinalClasses;

    private GenericMockitoStubber(boolean stubFinalClasses) {
        this.stubFinalClasses = stubFinalClasses;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        if (type.isEnum() || type.isArray() || type.isPrimitive()) {
            return false;
        }
        return stubFinalClasses || !isFinal(type);
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return accepts(context, type.getRawType());
    }

    @Override
    protected Object stubClass(StubbingContext context, Class<?> type) {
        return mock(type, withSettings().defaultAnswer(new StubbingAnswer(context)));
    }

    @Override
    protected Object stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return stub(context, type.getRawType());
    }

    private boolean isFinal(Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

}
