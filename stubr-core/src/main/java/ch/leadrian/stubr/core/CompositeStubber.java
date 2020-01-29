package ch.leadrian.stubr.core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class CompositeStubber extends Stubber {

    private final List<Stubber> stubbers;

    CompositeStubber(List<? extends Stubber> rootStubbers) {
        requireNonNull(rootStubbers, "stubbers");
        this.stubbers = ImmutableList.copyOf(rootStubbers);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        return stubbers.stream()
                .map(stubber -> stubber.tryToStub(type, context))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }

}
