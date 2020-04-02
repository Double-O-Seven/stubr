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

package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

/**
 * This class is a container for contextual information during a stubbing process.
 * <p>
 * Included are the {@link Stubber} performing the stubbing as well as the {@link StubbingSite} which indicates where
 * the requested stub value will be used.
 */
public final class StubbingContext {

    private static final EqualsAndHashCode<StubbingContext> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(StubbingContext.class)
            .compareAndHash(StubbingContext::getStubber)
            .compareAndHash(StubbingContext::getSite)
            .build();

    private final Stubber stubber;
    private final StubbingSite site;

    /**
     * Under normal circumstances it is not required to instantiate a {@link StubbingContext} manually, as {@link
     * Stubber}s will automatically create a suitable instance during the stubbing process.
     * <p>
     * However, a custom implementation of {@link Stubber} might override the {@link StubbingContext} in order to
     * enhance the given {@link Stubber} or {@link StubbingSite}.
     * <p>
     * {@code StubbingContext}s must be instantiated using a factory method for sake of extensibility and to keep the
     * option open to replace the concrete class with an interface or abstract class should the need arise.
     *
     * @param stubber the {@link Stubber} performing the stubbing
     * @param site    the {@link StubbingSite} where the requested stub value will be used
     */
    public static StubbingContext create(Stubber stubber, StubbingSite site) {
        return new StubbingContext(stubber, site);
    }

    private StubbingContext(Stubber stubber, StubbingSite site) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        this.stubber = stubber;
        this.site = site;
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
     * @param site the {@link StubbingSite} of the forked context
     * @return a copy of {@code this} with a potentially different {@link StubbingSite}
     */
    public StubbingContext fork(StubbingSite site) {
        return StubbingContext.create(stubber, site);
    }

    /**
     * @param stubber the {@link Stubber} of the forked context
     * @return a copy of {@code this} with a potentially different {@link StubbingSite}
     */
    public StubbingContext fork(Stubber stubber) {
        return StubbingContext.create(stubber, site);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

}
