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

package ch.leadrian.stubr.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StubbingStrategyTest {

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "false, true, false",
            "true, false, false",
            "false, false, false"
    })
    void whenStubberShouldAcceptIfAndOnlyIfReceiverStubberAndTypeMatcherAccept(boolean receiverAccepts, boolean typeMatches, boolean expectedResult) {
        StubbingStrategy stubbingStrategy = new StubbingStrategy() {

            @Override
            public boolean accepts(StubbingContext context, Type type) {
                return receiverAccepts;
            }

            @Override
            public Object stub(StubbingContext context, Type type) {
                return null;
            }
        };
        StubbingStrategy whenStubbingStrategy = stubbingStrategy.when(((context, type) -> typeMatches));

        boolean result = whenStubbingStrategy.accepts(mock(StubbingContext.class), String.class);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnStubValueOfReceiverStub() {
        StubbingStrategy stubbingStrategy = new StubbingStrategy() {

            @Override
            public boolean accepts(StubbingContext context, Type type) {
                return true;
            }

            @Override
            public Object stub(StubbingContext context, Type type) {
                return "Test";
            }
        };
        StubbingStrategy whenStubbingStrategy = stubbingStrategy.when(((context, type) -> true));

        Object result = whenStubbingStrategy.stub(mock(StubbingContext.class), String.class);

        assertThat(result)
                .isEqualTo("Test");
    }

}