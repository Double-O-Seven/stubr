/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.leadrian.stubr.junit;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class containing helper methods to process {@link ExtensionContext}s.
 */
public final class ExtensionContexts {

    private ExtensionContexts() {
    }

    /**
     * Returns a stream of {@link ExtensionContext}s starting from {@code context} and going up its chain of transitive
     * parents.
     *
     * @param context the extension context
     * @return a stream of all transitive parent of the given {@code context}, including the context itself
     */
    public static Stream<ExtensionContext> walk(ExtensionContext context) {
        return context.getParent()
                .map(parent -> Stream.concat(Stream.of(context), walk(parent)))
                .orElseGet(() -> Stream.of(context));
    }

    /**
     * Returns all annotations of type {@code annotationClass} that are present on the annotated elements of the {@code
     * contexts}.
     *
     * @param annotationClass the annotation type
     * @param contexts        list of contexts
     * @param <T>             the generic annotation type
     * @return all annotations of type {@code annotationClass}
     */
    public static <T extends Annotation> Stream<T> getAnnotations(Class<T> annotationClass, List<ExtensionContext> contexts) {
        Stream.Builder<T> annotations = Stream.builder();
        contexts.forEach(context -> getAnnotation(annotationClass, context).ifPresent(annotations::add));
        return annotations.build();
    }

    /**
     * Returns an annotation of type {@code annotationClass} if an annotated element is present on the given {@code
     * context} and such an annotation is present on the annotated element.
     *
     * @param annotationClass the annotation type
     * @param context         the extension context
     * @param <T>             the generic annotation type
     * @return the annotation if present
     */
    public static <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClass, ExtensionContext context) {
        return context.getElement().map(element -> element.getAnnotation(annotationClass));
    }

}
