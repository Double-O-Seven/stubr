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
    public Result<?> tryToStub(Type type, StubbingSite site) {
        for (RootStubber rootStubber : rootStubbers) {
            Result<?> result = rootStubber.tryToStub(type, site);
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failure();
    }

    @Override
    public Result<?> tryToStub(Parameter parameter, StubbingSite site) {
        for (RootStubber rootStubber : rootStubbers) {
            Result<?> result = rootStubber.tryToStub(parameter, site);
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failure();
    }
}
