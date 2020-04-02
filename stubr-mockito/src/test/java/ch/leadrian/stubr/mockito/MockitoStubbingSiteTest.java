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

package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnstableApiUsage")
class MockitoStubbingSiteTest {

    @Test
    void shouldReturnParent() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        StubbingSite expectedParent = mock(StubbingSite.class);
        MockitoStubbingSite site = new MockitoStubbingSite(expectedParent, invocation);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnInvocation() {
        InvocationOnMock expectedInvocation = mock(InvocationOnMock.class);
        StubbingSite parent = mock(StubbingSite.class);
        MockitoStubbingSite site = new MockitoStubbingSite(parent, expectedInvocation);

        InvocationOnMock invocation = site.getInvocation();

        assertThat(invocation)
                .isEqualTo(expectedInvocation);
    }

    @Test
    void shouldReturnMethodFromInvocation() {
        Method expectedMethod = mock(Method.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod())
                .thenReturn(expectedMethod);
        StubbingSite parent = mock(StubbingSite.class);
        MockitoStubbingSite site = new MockitoStubbingSite(parent, invocation);

        Method method = site.getMethod();

        assertThat(method)
                .isEqualTo(expectedMethod);
    }

    @Test
    void shouldReturnMethodAsAnnotatedElement() {
        Method expectedMethod = mock(Method.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod())
                .thenReturn(expectedMethod);
        StubbingSite parent = mock(StubbingSite.class);
        MockitoStubbingSite site = new MockitoStubbingSite(parent, invocation);

        AnnotatedElement annotatedElement = site.getAnnotatedElement();

        assertThat(annotatedElement)
                .isEqualTo(expectedMethod);
    }

    @Test
    void testEquals() {
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        StubbingSite parent1 = mock(StubbingSite.class);
        StubbingSite parent2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(new MockitoStubbingSite(parent1, invocation1), new MockitoStubbingSite(parent1, invocation1))
                .addEqualityGroup(new MockitoStubbingSite(parent2, invocation2), new MockitoStubbingSite(parent2, invocation2))
                .addEqualityGroup("test")
                .testEquals();
    }

}