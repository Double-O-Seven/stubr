package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingStrategy;

import java.util.function.Consumer;

public final class MockitoStubbers {

    private MockitoStubbers() {
    }

    public static StubbingStrategy mock(boolean stubFinalClasses) {
        return stubFinalClasses
                ? GenericMockitoStubbingStrategy.FINAL_STUBBING_INSTANCE
                : GenericMockitoStubbingStrategy.OPEN_ONLY_STUBBING_INSTANCE;
    }

    public static StubbingStrategy mock() {
        return mock(false);
    }

    public static <T> StubbingStrategy mock(Class<T> classToMock, Consumer<? super T> configurationAction) {
        return new MockitoStubbingStrategy<>(classToMock, configurationAction);
    }

    public static StubbingStrategy mock(Class<?> classToMock) {
        return mock(classToMock, null);
    }

}
