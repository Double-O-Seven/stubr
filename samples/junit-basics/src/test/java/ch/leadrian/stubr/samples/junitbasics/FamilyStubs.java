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

package ch.leadrian.stubr.samples.junitbasics;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.junit.StubberProvider;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;
import java.util.Random;

import static ch.leadrian.stubr.javafaker.FakerStrategies.firstName;
import static ch.leadrian.stubr.javafaker.FakerStrategies.lastName;
import static ch.leadrian.stubr.javafaker.JavaFakerStubbingStrategies.faked;
import static java.util.Collections.singletonList;

public final class FamilyStubs implements StubberProvider {

    @Override
    public List<? extends Stubber> getStubbers(ExtensionContext extensionContext) {
        int seed = getStableTestMethodHash(extensionContext);
        Random random = new Random(seed);
        Stubber stubber = Stubber.builder()
                .stubWith(faked(firstName(), random))
                .stubWith(faked(lastName(), random))
                .build();
        return singletonList(stubber);
    }

    private int getStableTestMethodHash(ExtensionContext extensionContext) {
        return extensionContext.getTestMethod()
                .map(method -> method.getName().hashCode())
                .orElseThrow(() -> new IllegalStateException("No test method available"));
    }

}
