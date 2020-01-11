package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;

public interface RootStubber {

    static RootStubberBuilder builder() {
        return new RootStubberImpl.Builder();
    }

    static RootStubber compose(RootStubber... rootStubbers) {
        List<RootStubber> reversedRootStubbers = new ArrayList<>(asList(rootStubbers));
        reverse(reversedRootStubbers);
        return new CompositeRootStubber(reversedRootStubbers);
    }

    Result<?> tryToStub(Type type);

    default Object stub(Type type) {
        Result<?> result = tryToStub(type);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    Result<?> tryToStub(Parameter parameter);

    default Object stub(Parameter parameter) {
        Result<?> result = tryToStub(parameter);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub value %s", parameter));
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
