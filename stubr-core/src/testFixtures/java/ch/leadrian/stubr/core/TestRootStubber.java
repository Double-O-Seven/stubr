package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class TestRootStubber extends RootStubber {

    private final Map<Type, ResultProvider> resultProvidersByType;

    TestRootStubber(Map<Type, ResultProvider> resultProvidersByType) {
        requireNonNull(resultProvidersByType, "resultProvidersByType");
        this.resultProvidersByType = new HashMap<>(resultProvidersByType);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        ResultProvider resultProvider = resultProvidersByType.get(type);
        if (resultProvider == null) {
            throw new AssertionError(String.format("Unexpected type encountered: %s", type));
        }
        return resultProvider.get();
    }

}
