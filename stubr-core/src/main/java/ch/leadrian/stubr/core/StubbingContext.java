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

package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

/**
 * This class is a container for contextual information during a stubbing process.
 * <p>
 * Included are the {@link Stubber} performing the stubbing as well as the {@link StubbingSite} which indicates where
 * the requested stub value will be used.
 */
public final class StubbingContext {

    private final Stubber stubber;
    private final StubbingSite site;
    private final StubberChain chain;

    StubbingContext(Stubber stubber, StubbingSite site, Type type) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        requireNonNull(type, "type");
        this.stubber = stubber;
        this.site = site;
        this.chain = stubber.newChain(type, this);
    }

    /**
     * @return the {@link Stubber} performing the stubbing
     */
    public Stubber getStubber() {
        return stubber;
    }

    /**
     * @return the {@link StubbingSite} where the requested stub value will be used
     */
    public StubbingSite getSite() {
        return site;
    }

    /**
     * @return the {@link StubberChain} that is used to request a stub for a given type
     */
    public StubberChain getChain() {
        return chain;
    }

}
