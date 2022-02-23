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

package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubberBuilder;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.StubWith;
import ch.leadrian.stubr.junit.annotation.StubberBaseline;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static ch.leadrian.stubr.internal.com.google.common.collect.Lists.reverse;
import static ch.leadrian.stubr.junit.ExtensionContexts.getAnnotations;
import static ch.leadrian.stubr.junit.ExtensionContexts.walk;
import static java.util.stream.Collectors.toList;

final class StubberFactory {

    Stubber create(ExtensionContext context) {
        List<ExtensionContext> contexts = walk(context).collect(toList());
        StubberBuilder builder = getStubberBuilder(contexts);
        getStubbers(context, contexts).forEach(builder::include);
        getStubbingStrategies(context, contexts).forEach(builder::stubWith);
        return builder.build();
    }

    private StubberBuilder getStubberBuilder(List<ExtensionContext> contexts) {
        return getAnnotations(StubberBaseline.class, contexts)
                .findFirst()
                .map(StubberBaseline::value)
                .orElse(StubberBaseline.Variant.DEFAULT)
                .getBuilder();
    }

    private Stream<Stubber> getStubbers(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(Include.class, reverse(contexts))
                .map(Include::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getStubbers(context).stream());
    }

    private Stream<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext context, List<ExtensionContext> contexts) {
        return getAnnotations(StubWith.class, reverse(contexts))
                .map(StubWith::value)
                .flatMap(Arrays::stream)
                .map(this::newInstance)
                .flatMap(provider -> provider.getStubbingStrategies(context).stream());
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
