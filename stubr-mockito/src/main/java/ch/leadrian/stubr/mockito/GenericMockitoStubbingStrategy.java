package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.strategy.SimpleStubbingStrategy;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

final class GenericMockitoStubbingStrategy extends SimpleStubbingStrategy<Object> {

    static final GenericMockitoStubbingStrategy FINAL_STUBBING_INSTANCE = new GenericMockitoStubbingStrategy(true);
    static final GenericMockitoStubbingStrategy OPEN_ONLY_STUBBING_INSTANCE = new GenericMockitoStubbingStrategy(false);

    private final boolean stubFinalClasses;

    private GenericMockitoStubbingStrategy(boolean stubFinalClasses) {
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
