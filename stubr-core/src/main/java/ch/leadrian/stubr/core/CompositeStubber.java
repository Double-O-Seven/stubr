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

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Objects.requireNonNull;

final class CompositeStubber extends Stubber {

    private final List<Stubber> stubbers;

    CompositeStubber(List<? extends Stubber> stubbers) {
        requireNonNull(stubbers, "stubbers");
        this.stubbers = ImmutableList.copyOf(stubbers);
    }

    @Override
    protected Result<?> tryToStub(Type type, StubbingContext context) {
        return stubbers.stream()
                .map(stubber -> stubber.tryToStub(type, context))
                .filter(Result::isSuccess)
                .findFirst()
                .orElse(Result.failure());
    }

}
