package ch.leadrian.stubr.core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class CompositeRootStubber extends RootStubber {

    private final List<RootStubber> rootStubbers;

    CompositeRootStubber(List<? extends RootStubber> rootStubbers) {
        requireNonNull(rootStubbers, "rootStubbers");
        this.rootStubbers = ImmutableList.copyOf(rootStubbers);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        return rootStubbers.stream()
                .map(rootStubber -> rootStubber.tryToStub(type, context))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }

}
