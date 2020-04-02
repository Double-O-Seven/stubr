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

package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyProvidesStub implements StubbingStrategyTest {

    private final Type acceptedType;
    private final Object expectedValue;

    StubbingStrategyProvidesStub(Type acceptedType, Object expectedValue) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
        this.expectedValue = expectedValue;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = String.format("%s should provide %s as stub for %s", stubbingStrategy.getClass().getSimpleName(), expectedValue, acceptedType);
        return dynamicTest(displayName, () -> {
            Object value = stubbingStrategy.stub(context, acceptedType);

            if (expectedValue == null) {
                assertThat(value).isNull();
            } else {
                assertThat(value)
                        .hasSameClassAs(expectedValue)
                        .isEqualTo(expectedValue);
            }
        });
    }

}
