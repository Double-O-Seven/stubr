package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.stubber.SimpleStubber;

import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

final class MockitoStubber<T> extends SimpleStubber<T> {

    private final Class<T> classToMock;
    private final Consumer<? super T> configurationAction;

    MockitoStubber(Class<T> classToMock, Consumer<? super T> configurationAction) {
        requireNonNull(classToMock, "classToMock");
        this.classToMock = classToMock;
        this.configurationAction = configurationAction;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return type == classToMock;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return type.getRawType() == classToMock;
    }

    @Override
    protected T stubClass(StubbingContext context, Class<?> type) {
        return createMock(context);
    }

    @Override
    protected T stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return createMock(context);
    }

    private T createMock(StubbingContext context) {
        T mock = mock(classToMock, withSettings().defaultAnswer(new StubbingAnswer(context)));
        if (configurationAction != null) {
            configurationAction.accept(mock);
        }
        return mock;
    }
}
