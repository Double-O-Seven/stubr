package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class TestStubber extends Stubber {

    private final Map<Type, ResultProvider> resultProvidersByType;

    TestStubber(Map<Type, ResultProvider> resultProvidersByType) {
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
