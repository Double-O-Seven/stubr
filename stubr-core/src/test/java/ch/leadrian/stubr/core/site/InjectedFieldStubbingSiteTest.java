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
import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class InjectedFieldStubbingSiteTest {

    @Test
    void shouldReturnParent() throws Exception {
        StubbingSite expectedParent = mock(StubbingSite.class);
        Field field = InjectedFieldStubbingSiteTest.Foo.class.getField("foo");
        InjectedFieldStubbingSite site = StubbingSites.injectedField(expectedParent, field);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnField() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Field expectedField = InjectedFieldStubbingSiteTest.Foo.class.getField("foo");
        InjectedFieldStubbingSite site = StubbingSites.injectedField(parent, expectedField);

        Field field = site.getField();

        assertThat(field)
                .isEqualTo(expectedField);
    }

    @Test
    void shouldReturnFieldAsAnnotatedElement() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Field field = InjectedFieldStubbingSiteTest.Foo.class.getField("foo");
        InjectedFieldStubbingSite site = StubbingSites.injectedField(parent, field);

        AnnotatedElement element = site.getAnnotatedElement();

        assertThat(element)
                .isEqualTo(field);
    }

    @Test
    void shouldReturnFieldName() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        String fieldName = "foo";
        Field field = InjectedFieldStubbingSiteTest.Foo.class.getField(fieldName);
        InjectedFieldStubbingSite site = StubbingSites.injectedField(parent, field);

        String name = site.getName();

        assertThat(name)
                .isEqualTo(fieldName);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() throws Exception {
        StubbingSite parent1 = mock(StubbingSite.class);
        Field field1 = InjectedFieldStubbingSiteTest.Foo.class.getField("foo");
        StubbingSite parent2 = mock(StubbingSite.class);
        Field field2 = InjectedFieldStubbingSiteTest.Foo.class.getField("bar");

        new EqualsTester()
                .addEqualityGroup(StubbingSites.injectedField(parent1, field1), StubbingSites.injectedField(parent1, field1))
                .addEqualityGroup(StubbingSites.injectedField(parent2, field2), StubbingSites.injectedField(parent2, field2))
                .testEquals();
    }

    static class Foo {

        public String foo;

        public String bar;

    }

}