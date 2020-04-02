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

package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingStrategy;

import java.util.function.Consumer;

/**
 * Collection of factory methods to create {@link ch.leadrian.stubr.core.Stubber}s that use Mockito.
 */
public final class MockitoStubbingStrategies {

    private MockitoStubbingStrategies() {
    }

    /**
     * Returns a {@link StubbingStrategy} that provides stubs using mocks. The mock will return a stub value on every
     * non-void method call.
     * <p>
     * If {@code stubFinalClasses} is set to {@code true}, the strategy will accept final classes, else, only non-final
     * classes will be accepted and stubbed.
     *
     * @param stubFinalClasses flag indicating whether final classes should be accepted or not
     * @return a {@link StubbingStrategy} that provides stubs using mocks
     */
    public static StubbingStrategy mock(boolean stubFinalClasses) {
        return stubFinalClasses
                ? GenericMockStubbingStrategy.FINAL_STUBBING_INSTANCE
                : GenericMockStubbingStrategy.OPEN_ONLY_STUBBING_INSTANCE;
    }

    /**
     * Returns a {@link StubbingStrategy} that provides stubs using mocks. The mock will return a stub value on every
     * non-void method call.
     * <p>
     * The returned strategy does not accept final classes.
     *
     * @return a {@link StubbingStrategy} that provides stubs using mocks
     */
    public static StubbingStrategy mock() {
        return mock(false);
    }

    /**
     * Returns a {@link StubbingStrategy} that provides stub for {@code classToMock} using mocks. The mocks will return
     * a stub value on every non-void method call.
     * <p>
     * The returned strategy does not accept final classes.
     *
     * @param classToMock         the class to be stubbed using mocks
     * @param configurationAction action that can be used to perform additional configuration on the mock instance
     * @param <T>                 the type representing the class to be stubbed
     * @return a {@link StubbingStrategy} that provides stubs using mocks
     */
    public static <T> StubbingStrategy mock(Class<T> classToMock, Consumer<? super T> configurationAction) {
        return new MockStubbingStrategy<>(classToMock, configurationAction);
    }

    /**
     * Returns a {@link StubbingStrategy} that provides stub for {@code classToMock} using mocks. The mocks will return
     * a stub value on every non-void method call.
     * <p>
     * The returned strategy does not accept final classes.
     *
     * @param classToMock the class to be stubbed using mocks
     * @return a {@link StubbingStrategy} that provides stubs using mocks
     */
    public static StubbingStrategy mock(Class<?> classToMock) {
        return mock(classToMock, null);
    }

}
