package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

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

    Result<?> tryToStub(Type type, StubbingSite site);

    default Result<?> tryToStub(Type type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    default Object stub(Type type, StubbingSite site) {
        Result<?> result = tryToStub(type, site);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    default Object stub(Type type) {
        return stub(type, StubbingSites.unknown());
    }

    Result<?> tryToStub(Parameter parameter, StubbingSite site);

    default Result<?> tryToStub(Parameter parameter) {
        return tryToStub(parameter, StubbingSites.unknown());
    }

    default Object stub(Parameter parameter, StubbingSite site) {
        Result<?> result = tryToStub(parameter, site);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub value %s", parameter));
        }
        return result.getValue();
    }

    default Object stub(Parameter parameter) {
        return tryToStub(parameter, StubbingSites.unknown());
    }

    default <T> Result<T> tryToStub(Class<T> classToStub, StubbingSite site) {
        return tryToStub((Type) classToStub, site).map(classToStub::cast);
    }

    default <T> Result<T> tryToStub(Class<T> classToStub) {
        return tryToStub(classToStub, StubbingSites.unknown());
    }

    default <T> T stub(Class<T> classToStub, StubbingSite site) {
        return classToStub.cast(stub((Type) classToStub, site));
    }

    default <T> T stub(Class<T> classToStub) {
        return stub(classToStub, StubbingSites.unknown());
    }

}
