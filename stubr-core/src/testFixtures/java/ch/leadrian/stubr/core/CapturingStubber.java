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

package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

final class CapturingStubber extends Stubber {

    private final List<StubbingSite> capturedSites = new ArrayList<>();
    private final Stubber delegate;

    CapturingStubber(Stubber delegate) {
        this.delegate = delegate;
    }

    @Override
    StubberChain newChain(Type type, StubbingContext context) {
        StubbingSite site = context.getSite();
        capturedSites.add(site);
        return delegate.newChain(type, context);
    }

    public List<StubbingSite> getCapturedSites() {
        return capturedSites;
    }

}


