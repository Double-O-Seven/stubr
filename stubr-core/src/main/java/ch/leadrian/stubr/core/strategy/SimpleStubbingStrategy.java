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

package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.type.TypeVisitor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static ch.leadrian.stubr.core.type.TypeVisitor.accept;
import static ch.leadrian.stubr.core.type.Types.getBound;

/**
 * A base class for implementing stubbers that only accept and stub {@link Class}es or {@link ParameterizedType}s.
 *
 * @param <T> type of the stubbed value.
 */
public abstract class SimpleStubbingStrategy<T> implements StubbingStrategy {

    /**
     * Accepts a given {@link Type} if one of the following criteria is satisfied:
     * <ul>
     * <li>The given {@code type} is a {@link Class} and the implementation of {@link
     * SimpleStubbingStrategy#acceptsClass(StubbingContext, Class)} returns {@code true}</li>
     * <li>The given {@code type} is a {@link ParameterizedType} and the implementation of {@link
     * SimpleStubbingStrategy#acceptsParameterizedType(StubbingContext, ParameterizedType)} returns {@code true}</li>
     * <li>The given {@code type} is a {@link WildcardType} and the the bound of the wildcard is either a {@link Class}
     * or {@link ParameterizedType} that is accepted.
     * </ul>
     * <p>
     * {@link TypeVariable}s or {@link GenericArrayType}s are not accepted.
     * <p>
     * In case {@code type} is a {@link Class}, the implementation of {@link SimpleStubbingStrategy#acceptsClass(StubbingContext,
     * Class)} determines whether the {@code type} is accepted.
     * <p>
     * In case {@code type} is a {@link ParameterizedType}, the implementation of {@link
     * SimpleStubbingStrategy#acceptsParameterizedType(StubbingContext, ParameterizedType)}  determines whether the
     * {@code type} is accepted.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return {@code true} if the strategy can provide a suitable stub value for the given {@code type}, else {@code
     * false}
     * @see SimpleStubbingStrategy#acceptsClass(StubbingContext, Class)
     * @see SimpleStubbingStrategy#acceptsParameterizedType(StubbingContext, ParameterizedType)
     */
    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return acceptsClass(context, clazz);
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return acceptsParameterizedType(context, parameterizedType);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getBound(wildcardType)
                        .filter(t -> accept(t, this))
                        .isPresent();
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return false;
            }

            @Override
            public Boolean visit(GenericArrayType genericArrayType) {
                return false;
            }
        });
    }

    /**
     * Method that determines whether {@code this} strategy can provide a suitable stub value for {@code type}, given
     * the {@code context}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return {@code true} if the strategy can provide a suitable stub value for the given {@code type}, else {@code
     * false}
     * @see SimpleStubbingStrategy#accepts(StubbingContext, Type)
     * @see StubbingStrategy#accepts(StubbingContext, Type)
     */
    protected abstract boolean acceptsClass(StubbingContext context, Class<?> type);

    /**
     * Method that determines whether {@code this} strategy can provide a suitable stub value for {@code type}, given
     * the {@code context}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return {@code true} if the strategy can provide a suitable stub value for the given {@code type}, else {@code
     * false}
     * @see SimpleStubbingStrategy#accepts(StubbingContext, Type)
     * @see StubbingStrategy#accepts(StubbingContext, Type)
     */
    protected abstract boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type);

    /**
     * Delegates the stubbing of {@link Class}es to {@link SimpleStubbingStrategy#stubClass(StubbingContext, Class)} and
     * the stubbing of {@link ParameterizedType}s to {@link SimpleStubbingStrategy#stubParameterizedType(StubbingContext,
     * ParameterizedType)}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return a suitable stub value for the given {@code type}
     */
    @Override
    public T stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return stubClass(context, clazz);
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                return stubParameterizedType(context, parameterizedType);
            }

            @Override
            public T visit(WildcardType wildcardType) {
                return getBound(wildcardType)
                        .map(t -> accept(t, this))
                        .orElseThrow(() -> new StubbingException(context.getSite(), wildcardType));
            }

            @Override
            public T visit(TypeVariable<?> typeVariable) {
                throw new StubbingException(context.getSite(), typeVariable);
            }

            @Override
            public T visit(GenericArrayType genericArrayType) {
                throw new StubbingException(context.getSite(), genericArrayType);
            }
        });
    }

    /**
     * Returns a suitable value for the given {@code type}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return a suitable stub value for the given {@code type}
     */
    protected abstract T stubClass(StubbingContext context, Class<?> type);

    /**
     * Returns a suitable value for the given {@code type}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return a suitable stub value for the given {@code type}
     */
    protected abstract T stubParameterizedType(StubbingContext context, ParameterizedType type);

}
