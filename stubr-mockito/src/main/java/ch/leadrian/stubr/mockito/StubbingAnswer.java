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

package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;

final class StubbingAnswer implements Answer<Object> {

    private final StubbingContext context;

    StubbingAnswer(StubbingContext context) {
        this.context = context;
    }

    @Override
    public Object answer(InvocationOnMock invocation) {
        Type returnType = invocation.getMethod().getReturnType();
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }
        MockitoStubbingSite site = new MockitoStubbingSite(context.getSite(), invocation);
        return context.getStubber().stub(returnType, site);
    }

}
