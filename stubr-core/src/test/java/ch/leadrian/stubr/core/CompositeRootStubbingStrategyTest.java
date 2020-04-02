/*
 * Copyright (C) 2020 Adrian-Philipp Leuenberger
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

package ch.leadrian.stubr.core;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CompositeRootStubbingStrategyTest {

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnResultOfFirstSuccessfulStubber() {
        Class<String> type = String.class;
        StubbingContext context = mock(StubbingContext.class);
        Stubber stubber1 = mock(Stubber.class);
        when(stubber1.tryToStub(type, context))
                .thenReturn(Result.failure());
        Stubber stubber2 = mock(Stubber.class);
        when(stubber2.tryToStub(type, context))
                .thenReturn((Result) Result.success("Test"));
        Stubber stubber3 = mock(Stubber.class);
        when(stubber3.tryToStub(type, context))
                .thenReturn((Result) Result.success("Foo"));
        Stubber compositeStubber = Stubber.compose(stubber1, stubber2, stubber3);

        Result<?> result = compositeStubber.tryToStub(type, context);

        assertAll(
                () -> assertThat(result.getValue()).isEqualTo("Test"),
                () -> {
                    InOrder inOrder = inOrder(stubber1, stubber2);
                    inOrder.verify(stubber1).tryToStub(type, context);
                    inOrder.verify(stubber2).tryToStub(type, context);
                },
                () -> verifyNoInteractions(stubber3)
        );
    }

}