package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.Stubber;

import java.util.function.Consumer;

public final class MockitoStubbers {

    private MockitoStubbers() {
    }

    public static Stubber mock(boolean stubFinalClasses) {
        return stubFinalClasses
                ? GenericMockitoStubber.FINAL_STUBBING_INSTANCE
                : GenericMockitoStubber.OPEN_ONLY_STUBBING_INSTANCE;
    }

    public static Stubber mock() {
        return mock(false);
    }

    public static <T> Stubber mock(Class<T> classToMock, Consumer<? super T> configurationAction) {
        return new MockitoStubber<>(classToMock, configurationAction);
    }

    public static Stubber mock(Class<?> classToMock) {
        return mock(classToMock, null);
    }

}
