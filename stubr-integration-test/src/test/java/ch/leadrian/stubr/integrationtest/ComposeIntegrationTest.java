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

package ch.leadrian.stubr.integrationtest;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.strategy.EnhancingStubbingStrategy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Locale;

import static ch.leadrian.stubr.core.matcher.Matchers.equalTo;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.constantValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ComposeIntegrationTest {

    @Test
    void shouldEnhanceOnCompositeStubber() {
        Stubber stubber = Stubber.builder()
                .include(Stubber.builder()
                        .stubWith(constantValue("Bar"))
                        .stubWith(constantValue(123))
                        .stubWith(constantValue(Locale.GERMAN))
                        .build())
                .include(Stubber.builder()
                        .stubWith(constantValue("Foo"))
                        .stubWith(new EnhancingStubbingStrategy() {

                            @Override
                            protected Object enhance(StubbingContext context, Type type, Object stubValue) {
                                return ((String) stubValue).toUpperCase();
                            }
                        }.when(equalTo(String.class)))
                        .stubWith(new EnhancingStubbingStrategy() {

                            @Override
                            protected Object enhance(StubbingContext context, Type type, Object stubValue) {
                                return -((Integer) stubValue);
                            }
                        }.when(equalTo(Integer.class)))
                        .build())
                .build();

        assertAll(
                () -> assertThat(stubber.stub(String.class)).isEqualTo("FOO"),
                () -> assertThat(stubber.stub(Integer.class)).isEqualTo(-123),
                () -> assertThat(stubber.stub(Locale.class)).isEqualTo(Locale.GERMAN)
        );
    }

}
