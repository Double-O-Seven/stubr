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

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyStubsAtSite implements StubbingStrategyTestCase {

    private final Type acceptedType;
    private final List<StubbingSite> expectedSites;

    StubbingStrategyStubsAtSite(Type acceptedType, List<StubbingSite> expectedSites) {
        requireNonNull(acceptedType, "acceptedType");
        requireNonNull(expectedSites, "expectedSites");
        this.acceptedType = acceptedType;
        this.expectedSites = new ArrayList<>(expectedSites);
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, Stubber stubber, StubbingSite site) {
        String displayName = getDisplayName(stubbingStrategy);
        return dynamicTest(displayName, () -> {
            CapturingStubber capturingStubber = new CapturingStubber(stubber);

            capturingStubber.tryToStub(acceptedType, site);

            List<StubbingSite> allExpectedSites = new ArrayList<>(this.expectedSites);
            allExpectedSites.add(0, site);
            assertThat(capturingStubber.getCapturedSites())
                    .containsExactlyElementsOf(allExpectedSites);
        });
    }

    private String getDisplayName(StubbingStrategy stubbingStrategy) {
        String sites = expectedSites
                .stream()
                .map(Object::toString)
                .collect(joining(", "));
        return String.format("%s should stub %s at %s", stubbingStrategy.getClass().getSimpleName(), acceptedType, sites);
    }

}
