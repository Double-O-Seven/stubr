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
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class TestStubber extends Stubber {

    private final Map<Type, ResultProvider> resultProvidersByType;

    TestStubber(Map<Type, ResultProvider> resultProvidersByType) {
        requireNonNull(resultProvidersByType, "resultProvidersByType");
        this.resultProvidersByType = new HashMap<>(resultProvidersByType);
    }

    @Override
    StubberChain newChain(Type type, StubbingContext context) {
        return new Chain(type);
    }

    private final class Chain implements StubberChain {

        private final Type type;

        private Chain(Type type) {
            this.type = type;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Result<?> next() {
            ResultProvider resultProvider = resultProvidersByType.get(type);
            if (resultProvider == null) {
                throw new AssertionError(String.format("Unexpected type encountered: %s", type));
            }
            return resultProvider.get();
        }

    }

}
