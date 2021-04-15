/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
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
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class ConstructorParameterStubbingSiteTest {

    @Test
    void shouldReturnParent() throws Exception {
        StubbingSite expectedParent = mock(StubbingSite.class);
        Constructor<Foo> constructor = Foo.class.getConstructor(int.class);
        Parameter parameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(expectedParent, constructor, parameter);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnConstructor() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> expectedConstructor = Foo.class.getConstructor(int.class);
        Parameter parameter = expectedConstructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, expectedConstructor, parameter);

        Constructor<?> constructor = site.getConstructor();

        assertThat(constructor)
                .isEqualTo(expectedConstructor);
    }

    @Test
    void shouldReturnConstructorAsExecutable() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> expectedConstructor = Foo.class.getConstructor(int.class);
        Parameter parameter = expectedConstructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, expectedConstructor, parameter);

        Executable executable = site.getExecutable();

        assertThat(executable)
                .isEqualTo(expectedConstructor);
    }

    @Test
    void shouldReturnParameter() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> constructor = Foo.class.getConstructor(int.class);
        Parameter expectedParameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, constructor, expectedParameter);

        Parameter parameter = site.getParameter();

        assertThat(parameter)
                .isEqualTo(expectedParameter);
    }

    @Test
    void shouldReturnParameterName() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> constructor = Foo.class.getConstructor(int.class);
        Parameter parameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, constructor, parameter);

        String parameterName = site.getName();

        assertThat(parameterName)
                .isEqualTo(parameter.getName());
    }

    @Test
    void shouldReturnParameterIndex() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> constructor = Foo.class.getConstructor(int.class);
        Parameter parameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, constructor, parameter);

        int index = site.getParameterIndex();

        assertThat(index)
                .isZero();
    }

    @Test
    void shouldReturnParameterAsAnnotatedElement() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Constructor<Foo> constructor = Foo.class.getConstructor(int.class);
        Parameter expectedParameter = constructor.getParameters()[0];
        ConstructorParameterStubbingSite site = StubbingSites.constructorParameter(parent, constructor, expectedParameter);

        AnnotatedElement annotatedElement = site.getAnnotatedElement();

        assertThat(annotatedElement)
                .isEqualTo(expectedParameter);
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() throws Exception {
        StubbingSite parent1 = mock(StubbingSite.class);
        Constructor<Foo> constructor1 = Foo.class.getConstructor(int.class);
        Parameter expectedParameter1 = constructor1.getParameters()[0];
        StubbingSite parent2 = mock(StubbingSite.class);
        Constructor<Foo> constructor2 = Foo.class.getConstructor(String.class);
        Parameter expectedParameter2 = constructor2.getParameters()[0];

        new EqualsTester()
                .addEqualityGroup(StubbingSites.constructorParameter(parent1, constructor1, expectedParameter1), StubbingSites.constructorParameter(parent1, constructor1, expectedParameter1))
                .addEqualityGroup(StubbingSites.constructorParameter(parent2, constructor2, expectedParameter2), StubbingSites.constructorParameter(parent2, constructor2, expectedParameter2))
                .testEquals();
    }

    @SuppressWarnings("unused")
    public static final class Foo {

        public Foo(int param) {
        }

        public Foo(String param) {
        }

    }

}