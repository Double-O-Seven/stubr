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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExtensionContextsTest {

    @Test
    void shouldReturnExtensionContextsInOrder() {
        ExtensionContext grandParent = mock(ExtensionContext.class);
        when(grandParent.getParent())
                .thenReturn(Optional.empty());
        ExtensionContext parent = mock(ExtensionContext.class);
        when(parent.getParent())
                .thenReturn(Optional.of(grandParent));
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getParent())
                .thenReturn(Optional.of(parent));

        Stream<ExtensionContext> contexts = ExtensionContexts.walk(context);

        assertThat(contexts)
                .containsExactly(context, parent, grandParent);
    }

    @Test
    void shouldReturnAnnotations() {
        ExtensionContext context1 = mock(ExtensionContext.class);
        when(context1.getElement())
                .thenReturn(Optional.of(Foo.class));
        ExtensionContext context2 = mock(ExtensionContext.class);
        when(context2.getElement())
                .thenReturn(Optional.of(Bar.class));
        ExtensionContext context3 = mock(ExtensionContext.class);
        when(context3.getElement())
                .thenReturn(Optional.of(Qux.class));

        Stream<Fubar> contexts = ExtensionContexts.getAnnotations(Fubar.class, asList(context1, context2, context3));

        assertThat(contexts)
                .extracting(Fubar::value)
                .containsExactly("foo", "qux");
    }

    @Test
    void shouldReturnAnnotation() {
        ExtensionContext context = mock(ExtensionContext.class);
        when(context.getElement())
                .thenReturn(Optional.of(Qux.class));

        Optional<Fubar> contexts = ExtensionContexts.getAnnotation(Fubar.class, context);

        assertThat(contexts)
                .hasValueSatisfying(fubar -> assertThat(fubar.value()).isEqualTo("qux"));
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface Fubar {

        String value();

    }

    @Fubar("foo")
    private static class Foo {
    }

    private static class Bar {
    }

    @Fubar("qux")
    private static class Qux {
    }

}