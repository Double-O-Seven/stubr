/*
 * Copyright (C) 2022 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

abstract class AnnotationMatcher<T extends AnnotatedElement> implements Matcher<T> {

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(String annotationName) {
        return new AnnotationMatcher.ByName<>(annotationName);
    }

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(Class<? extends Annotation> annotationType) {
        return new AnnotationMatcher.ByType<>(annotationType);
    }

    static <T extends AnnotatedElement> AnnotationMatcher<T> by(Annotation annotation) {
        return new AnnotationMatcher.ByEquality<>(annotation);
    }

    private AnnotationMatcher() {
    }

    private static final class ByName<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private final String annotationName;

        ByName(String annotationName) {
            requireNonNull(annotationName, "annotationName");
            this.annotationName = annotationName;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            return stream(value.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(type -> annotationName.equals(type.getName()) || annotationName.equals(type.getSimpleName()));
        }

    }

    private static final class ByType<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private final Class<? extends Annotation> annotationType;

        ByType(Class<? extends Annotation> annotationType) {
            requireNonNull(annotationType, "annotationType");
            this.annotationType = annotationType;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            return value.isAnnotationPresent(annotationType);
        }

    }

    private static final class ByEquality<T extends AnnotatedElement> extends AnnotationMatcher<T> {

        private final Annotation annotation;

        ByEquality(Annotation annotation) {
            requireNonNull(annotation, "annotation");
            this.annotation = annotation;
        }

        @Override
        public boolean matches(StubbingContext context, T value) {
            Annotation annotationOnElement = value.getAnnotation(annotation.annotationType());
            return annotation.equals(annotationOnElement);
        }

    }

}
