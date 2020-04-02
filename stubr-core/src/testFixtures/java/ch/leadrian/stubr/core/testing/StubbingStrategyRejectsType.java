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

final class StubbingStrategyRejectsType implements StubbingStrategyTest {

    private final Type acceptedType;

    StubbingStrategyRejectsType(Type acceptedType) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = String.format("%s should rejects %s", stubbingStrategy.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            boolean accepts = stubbingStrategy.accepts(context, acceptedType);

            assertThat(accepts).isFalse();
        });
    }

}
