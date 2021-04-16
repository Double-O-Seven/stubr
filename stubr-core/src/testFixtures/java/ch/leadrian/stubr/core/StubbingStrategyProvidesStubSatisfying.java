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

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyProvidesStubSatisfying implements StubbingStrategyTestCase {

    private final Type acceptedType;
    private final Consumer<Object> assertion;

    StubbingStrategyProvidesStubSatisfying(Type acceptedType, Consumer<Object> assertion) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
        this.assertion = assertion;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, Stubber stubber, StubbingSite site) {
        String displayName = String.format("%s should provide stub for %s", stubbingStrategy.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            Result<?> result = stubber.tryToStub(acceptedType, site);

            assertThat(result.isSuccess()).isTrue();
            Object value = result.getValue();
            assertion.accept(value);
        });
    }

}
