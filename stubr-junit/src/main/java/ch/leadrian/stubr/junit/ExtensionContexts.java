package ch.leadrian.stubr.junit;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class ExtensionContexts {

    private ExtensionContexts() {
    }

    public static Stream<ExtensionContext> walk(ExtensionContext context) {
        return context.getParent()
                .map(parent -> Stream.concat(Stream.of(context), walk(parent)))
                .orElseGet(() -> Stream.of(context));
    }

    public static <T extends Annotation> Stream<T> getAnnotations(Class<T> annotationClass, List<ExtensionContext> contexts) {
        Stream.Builder<T> annotations = Stream.builder();
        contexts.forEach(context -> getAnnotation(annotationClass, context).ifPresent(annotations::add));
        return annotations.build();
    }

    public static <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClass, ExtensionContext context) {
        return context.getElement().map(element -> element.getAnnotation(annotationClass));
    }

}
