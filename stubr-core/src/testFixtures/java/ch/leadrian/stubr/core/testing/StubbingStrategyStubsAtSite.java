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

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyStubsAtSite implements StubbingStrategyTest {

    private final Type acceptedType;
    private final List<StubbingSite> expectedSites;

    StubbingStrategyStubsAtSite(Type acceptedType, List<StubbingSite> expectedSites) {
        requireNonNull(acceptedType, "acceptedType");
        requireNonNull(expectedSites, "expectedSites");
        this.acceptedType = acceptedType;
        this.expectedSites = new ArrayList<>(expectedSites);
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = getDisplayName(stubbingStrategy);
        return dynamicTest(displayName, () -> {
            CapturingStubber capturingStubber = new CapturingStubber(context.getStubber());
            StubbingContext capturingContext = context.fork(capturingStubber);

            stubbingStrategy.stub(capturingContext, acceptedType);

            assertThat(capturingStubber.getCapturedSites())
                    .containsExactlyElementsOf(expectedSites);
        });
    }

    private String getDisplayName(StubbingStrategy stubbingStrategy) {
        String sites = expectedSites
                .stream()
                .map(Object::toString)
                .collect(joining(", "));
        return String.format("%s should stub %s at %s", stubbingStrategy.getClass().getSimpleName(), acceptedType, sites);
    }

    private static final class CapturingStubber extends Stubber {

        private final List<StubbingSite> capturedSites = new ArrayList<>();
        private final Stubber delegate;

        CapturingStubber(Stubber delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Result<?> tryToStub(Type type, StubbingContext context) {
            StubbingSite site = context.getSite();
            capturedSites.add(site);
            return delegate.tryToStub(type, site);
        }

        public List<StubbingSite> getCapturedSites() {
            return capturedSites;
        }

    }

}
