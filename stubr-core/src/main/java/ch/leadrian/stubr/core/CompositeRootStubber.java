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
        return rootStubbers.stream()
                .map(rootStubber -> rootStubber.tryToStub(type, site))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }

    @Override
    public Result<?> tryToStub(Parameter parameter, StubbingSite site) {
        return rootStubbers.stream()
                .map(rootStubber -> rootStubber.tryToStub(parameter, site))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }
}
