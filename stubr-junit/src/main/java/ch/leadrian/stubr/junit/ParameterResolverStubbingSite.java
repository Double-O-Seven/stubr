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

package ch.leadrian.stubr.junit;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.ParameterStubbingSite;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;

/**
 * Represent the stubbing site of a {@link org.junit.jupiter.api.extension.ParameterResolver#resolveParameter(ParameterContext,
 * ExtensionContext)} call.
 */
public final class ParameterResolverStubbingSite implements ParameterStubbingSite {

    private static final EqualsAndHashCode<ParameterResolverStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ParameterResolverStubbingSite.class)
            .compareAndHash(ParameterResolverStubbingSite::getParameterContext)
            .compareAndHash(ParameterResolverStubbingSite::getExtensionContext)
            .build();

    private final ParameterContext parameterContext;
    private final ExtensionContext extensionContext;

    ParameterResolverStubbingSite(ParameterContext parameterContext, ExtensionContext extensionContext) {
        this.parameterContext = parameterContext;
        this.extensionContext = extensionContext;
    }

    /**
     * Returns the {@link ParameterContext} of a {@link org.junit.jupiter.api.extension.ParameterResolver#resolveParameter(ParameterContext,
     * ExtensionContext)} call.
     *
     * @return the parameter context
     */
    public ParameterContext getParameterContext() {
        return parameterContext;
    }

    /**
     * Returns the {@link ExtensionContext} of a {@link org.junit.jupiter.api.extension.ParameterResolver#resolveParameter(ParameterContext,
     * ExtensionContext)} call.
     *
     * @return the extension context
     */
    public ExtensionContext getExtensionContext() {
        return extensionContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter getParameter() {
        return parameterContext.getParameter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getParameterIndex() {
        return parameterContext.getIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.empty();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("parameterContext", parameterContext)
                .add("extensionContext", extensionContext)
                .toString();
    }

}
