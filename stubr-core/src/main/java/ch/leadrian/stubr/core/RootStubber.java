package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

public interface RootStubber {

    static RootStubberBuilder builder() {
        return new RootStubberImpl.Builder();
    }

    Result<?> tryToStub(Type type);

    Object stub(Type type);

    <T> Result<T> tryToStub(Class<T> classToStub);

    <T> T stub(Class<T> classToStub);

}
