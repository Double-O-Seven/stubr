package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubberBuilder;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.RootStubberBaseline;
import ch.leadrian.stubr.junit.annotation.StubWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.junit.ExtensionContexts.getAnnotations;
import static ch.leadrian.stubr.junit.ExtensionContexts.walk;
import static com.google.common.collect.Lists.reverse;
import static java.util.stream.Collectors.toList;

final class RootStubberFactory {

    Stubber create(ExtensionContext context) {
        List<ExtensionContext> contexts = walk(context).collect(toList());
        StubberBuilder builder = getRootStubberBuilder(contexts);
        getRootStubbers(context, contexts).forEach(builder::include);
        getStubbers(context, contexts).forEach(builder::stubWith);
        return builder.build();
    }

    private StubberBuilder getRootStubberBuilder(List<ExtensionContext> contexts) {
        return getAnnotations(RootStubberBaseline.class, contexts)
                .findFirst()
                .map(RootStubberBaseline::value)
                .orElse(RootStubberBaseline.Variant.DEFAULT)
                .getBuilder();
    }

    private Stream<Stubber> getRootStubbers(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(Include.class, reverse(contexts))
                .map(Include::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getRootStubbers(context).stream());
    }

    private Stream<? extends StubbingStrategy> getStubbers(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(StubWith.class, reverse(contexts))
                .map(StubWith::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getStubbers(context).stream());
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
