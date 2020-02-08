package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubberBuilder;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.StubWith;
import ch.leadrian.stubr.junit.annotation.StubberBaseline;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.junit.ExtensionContexts.getAnnotations;
import static ch.leadrian.stubr.junit.ExtensionContexts.walk;
import static com.google.common.collect.Lists.reverse;
import static java.util.stream.Collectors.toList;

final class StubberFactory {

    Stubber create(ExtensionContext context) {
        List<ExtensionContext> contexts = walk(context).collect(toList());
        StubberBuilder builder = getStubberBuilder(contexts);
        getStubbers(context, contexts).forEach(builder::include);
        getStubbingStrategies(context, contexts).forEach(builder::stubWith);
        return builder.build();
    }

    private StubberBuilder getStubberBuilder(List<ExtensionContext> contexts) {
        return getAnnotations(StubberBaseline.class, contexts)
                .findFirst()
                .map(StubberBaseline::value)
                .orElse(StubberBaseline.Variant.DEFAULT)
                .getBuilder();
    }

    private Stream<Stubber> getStubbers(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(Include.class, reverse(contexts))
                .map(Include::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getStubbers(context).stream());
    }

    private Stream<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(StubWith.class, reverse(contexts))
                .map(StubWith::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getStubbingStrategies(context).stream());
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
