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

package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * Stubbing site indicating that the current stubbing site is a method parameter.
 */
public final class MethodParameterStubbingSite implements ParameterStubbingSite, MethodStubbingSite {

    private static final EqualsAndHashCode<MethodParameterStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodParameterStubbingSite.class)
            .compareAndHash(MethodParameterStubbingSite::getParent)
            .compareAndHash(MethodParameterStubbingSite::getMethod)
            .compareAndHash(MethodParameterStubbingSite::getParameter)
            .compareAndHashPrimitive(MethodParameterStubbingSite::getParameterIndex)
            .build();

    private final StubbingSite parent;
    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;

    MethodParameterStubbingSite(StubbingSite parent, Method method, Parameter parameter, int parameterIndex) {
        requireNonNull(parent, "parent");
        requireNonNull(method, "method");
        requireNonNull(parameter, "parameter");
        this.parent = parent;
        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getParameterIndex() {
        return parameterIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return parameter.getName();
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
                .add("parent", parent)
                .add("method", method)
                .add("parameter", parameter)
                .toString();
    }

}
