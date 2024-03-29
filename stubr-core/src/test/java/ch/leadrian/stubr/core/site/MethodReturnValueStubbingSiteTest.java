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

package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class MethodReturnValueStubbingSiteTest {

    @Test
    void shouldReturnParent() throws Exception {
        StubbingSite expectedParent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        MethodReturnValueStubbingSite site = StubbingSites.methodReturnValue(expectedParent, method);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnMethod() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method expectedMethod = Foo.class.getMethod("foo", int.class);
        MethodReturnValueStubbingSite site = StubbingSites.methodReturnValue(parent, expectedMethod);

        Method method = site.getMethod();

        assertThat(method)
                .isEqualTo(expectedMethod);
    }

    @Test
    void shouldReturnMethodName() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        MethodReturnValueStubbingSite site = StubbingSites.methodReturnValue(parent, method);

        String name = site.getName();

        assertThat(name)
                .isEqualTo("foo");
    }

    @Test
    void shouldReturnMethodAsExecutable() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method expectedMethod = Foo.class.getMethod("foo", int.class);
        MethodReturnValueStubbingSite site = StubbingSites.methodReturnValue(parent, expectedMethod);

        Executable executable = site.getExecutable();

        assertThat(executable)
                .isEqualTo(expectedMethod);
    }

    @Test
    void shouldReturnMethodAsAnnotatedElement() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method expectedMethod = Foo.class.getMethod("foo", int.class);
        MethodReturnValueStubbingSite site = StubbingSites.methodReturnValue(parent, expectedMethod);

        AnnotatedElement annotatedElement = site.getAnnotatedElement();

        assertThat(annotatedElement)
                .isEqualTo(expectedMethod);
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() throws Exception {
        StubbingSite parent1 = mock(StubbingSite.class);
        Method method1 = Foo.class.getMethod("foo", int.class);
        StubbingSite parent2 = mock(StubbingSite.class);
        Method method2 = Foo.class.getMethod("foo", String.class);

        new EqualsTester()
                .addEqualityGroup(StubbingSites.methodReturnValue(parent1, method1), StubbingSites.methodReturnValue(parent1, method1))
                .addEqualityGroup(StubbingSites.methodReturnValue(parent2, method2), StubbingSites.methodReturnValue(parent2, method2))
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