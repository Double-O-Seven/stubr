package ch.leadrian.stubr.core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.List;

final class CompositeRootStubber implements RootStubber {

    private final List<RootStubber> rootStubbers;

    CompositeRootStubber(List<? extends RootStubber> rootStubbers) {
        this.rootStubbers = ImmutableList.copyOf(rootStubbers);
    }

    @Override
    public Result<?> tryToStub(Type type, StubbingSite site) {
        return rootStubbers.stream()
                .map(rootStubber -> rootStubber.tryToStub(type, site))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }
}
