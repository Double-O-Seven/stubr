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

package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
class ParameterResolverStubbingSiteTest {

    @Test
    void shouldReturnEmptyParent() throws Exception {
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter parameter = method.getParameters()[0];
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.getParameter())
                .thenReturn(parameter);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .isEmpty();
    }

    @Test
    void shouldReturnParameter() throws Exception {
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter expectedParameter = method.getParameters()[0];
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.getParameter())
                .thenReturn(expectedParameter);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);

        Parameter parameter = site.getParameter();

        assertThat(parameter)
                .isEqualTo(expectedParameter);
    }

    @Test
    void shouldReturnParameterIndex() {
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.getIndex())
                .thenReturn(13);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);

        int index = site.getParameterIndex();

        assertThat(index)
                .isEqualTo(13);
    }

    @Test
    void shouldReturnParameterContext() throws Exception {
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter parameter = method.getParameters()[0];
        ParameterContext expectedParameterContext = mock(ParameterContext.class);
        when(expectedParameterContext.getParameter())
                .thenReturn(parameter);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(expectedParameterContext, extensionContext);

        ParameterContext parameterContext = site.getParameterContext();

        assertThat(parameterContext)
                .isEqualTo(expectedParameterContext);
    }

    @Test
    void shouldReturnExtensionContext() throws Exception {
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter parameter = method.getParameters()[0];
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.getParameter())
                .thenReturn(parameter);
        ExtensionContext expectedExtensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, expectedExtensionContext);

        ExtensionContext extensionContext = site.getExtensionContext();

        assertThat(extensionContext)
                .isEqualTo(expectedExtensionContext);
    }

    @Test
    void shouldReturnParameterAsAnnotatedElement() throws Exception {
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter expectedParameter = method.getParameters()[0];
        ParameterContext parameterContext = mock(ParameterContext.class);
        when(parameterContext.getParameter())
                .thenReturn(expectedParameter);
        ExtensionContext extensionContext = mock(ExtensionContext.class);
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);

        AnnotatedElement annotatedElement = site.getAnnotatedElement();

        assertThat(annotatedElement)
                .isEqualTo(expectedParameter);
    }

    @Test
    void testEquals() {
        ParameterContext parameterContext1 = mock(ParameterContext.class);
        ExtensionContext extensionContext1 = mock(ExtensionContext.class);
        ParameterContext parameterContext2 = mock(ParameterContext.class);
        ExtensionContext extensionContext2 = mock(ExtensionContext.class);

        new EqualsTester()
                .addEqualityGroup(new ParameterResolverStubbingSite(parameterContext1, extensionContext1), new ParameterResolverStubbingSite(parameterContext1, extensionContext1))
                .addEqualityGroup(new ParameterResolverStubbingSite(parameterContext1, extensionContext2), new ParameterResolverStubbingSite(parameterContext1, extensionContext2))
                .addEqualityGroup(new ParameterResolverStubbingSite(parameterContext2, extensionContext2), new ParameterResolverStubbingSite(parameterContext2, extensionContext2))
                .addEqualityGroup("String")
                .testEquals();
    }

    @SuppressWarnings("unused")
    public static final class Foo {

        public void foo(int param) {
        }

        public void foo(String param) {
        }

    }

}