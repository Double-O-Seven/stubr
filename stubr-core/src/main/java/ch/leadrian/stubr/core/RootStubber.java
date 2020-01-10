package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.Optional;

public interface RootStubber {

    Optional<?> tryToStub(Type type);

    default Object stub(Type type) {
        return tryToStub(type).orElseThrow(() -> new IllegalStateException(String.format("Failed to stub instance of %s", type)));
    }

    default <T> Optional<T> tryToStub(Class<T> clazz) {
        return tryToStub((Type) clazz).map(clazz::cast);
    }

    default <T> T stub(Class<T> clazz) {
        return clazz.cast(stub((Type) clazz));
    }

}
