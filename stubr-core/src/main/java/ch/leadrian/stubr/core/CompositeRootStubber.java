package ch.leadrian.stubr.core;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;

final class CompositeRootStubber implements RootStubber {

    private final List<RootStubber> rootStubbers;

    CompositeRootStubber(List<RootStubber> rootStubbers) {
        this.rootStubbers = rootStubbers;
    }

    @Override
    public Result<?> tryToStub(Type type) {
        for (RootStubber rootStubber : rootStubbers) {
            Result<?> result = rootStubber.tryToStub(type);
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failure();
    }

    @Override
    public Result<?> tryToStub(Parameter parameter) {
        for (RootStubber rootStubber : rootStubbers) {
            Result<?> result = rootStubber.tryToStub(parameter);
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failure();
    }
}
