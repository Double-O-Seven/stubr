package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

public interface RootStubber {

    static RootStubberBuilder builder() {
        return new RootStubberImpl.Builder();
    }

    Result<?> tryToStub(Type type);

    default Object stub(Type type) {
        Result<?> result = tryToStub(type);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    default <T> Result<T> tryToStub(Class<T> classToStub) {
        return tryToStub((Type) classToStub).map(classToStub::cast);
    }

    default <T> T stub(Class<T> classToStub) {
        return classToStub.cast(stub((Type) classToStub));
    }

}
