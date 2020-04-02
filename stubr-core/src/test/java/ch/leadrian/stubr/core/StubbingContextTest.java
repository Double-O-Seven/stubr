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

package ch.leadrian.stubr.core;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class StubbingContextTest {

    @Test
    void shouldCreateContextWithArguments() {
        Stubber stubber = mock(Stubber.class);
        StubbingSite site = mock(StubbingSite.class);

        StubbingContext context = StubbingContext.create(stubber, site);

        assertAll(
                () -> assertThat(context.getStubber()).isEqualTo(stubber),
                () -> assertThat(context.getSite()).isEqualTo(site)
        );
    }

    @Test
    void shouldForkWithSite() {
        Stubber stubber = mock(Stubber.class);
        StubbingSite site1 = mock(StubbingSite.class);
        StubbingSite site2 = mock(StubbingSite.class);
        StubbingContext context = StubbingContext.create(stubber, site1);

        StubbingContext forkedContext = context.fork(site2);

        assertThat(forkedContext)
                .isNotSameAs(context)
                .isEqualTo(StubbingContext.create(stubber, site2));
    }

    @Test
    void shouldForkWithStubber() {
        Stubber stubber1 = mock(Stubber.class);
        Stubber stubber2 = mock(Stubber.class);
        StubbingSite site = mock(StubbingSite.class);
        StubbingContext context = StubbingContext.create(stubber1, site);

        StubbingContext forkedContext = context.fork(stubber2);

        assertThat(forkedContext)
                .isNotSameAs(context)
                .isEqualTo(StubbingContext.create(stubber2, site));
    }

    @Test
    void testEquals() {
        Stubber stubber1 = mock(Stubber.class);
        StubbingSite site1 = mock(StubbingSite.class);
        Stubber stubber2 = mock(Stubber.class);
        StubbingSite site2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(StubbingContext.create(stubber1, site1), StubbingContext.create(stubber1, site1))
                .addEqualityGroup(StubbingContext.create(stubber2, site2), StubbingContext.create(stubber2, site2))
                .addEqualityGroup("Test")
                .testEquals();
    }

}