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

package ch.leadrian.stubr.javafaker;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import ch.leadrian.stubr.core.site.NamedStubbingSite;
import com.github.javafaker.Faker;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

final class FakerStubbingStrategy implements StubbingStrategy {

    private final Faker faker;
    private final FakerStrategy strategy;

    FakerStubbingStrategy(Faker faker, FakerStrategy strategy) {
        requireNonNull(faker, "faker");
        requireNonNull(strategy, "strategy");
        this.faker = faker;
        this.strategy = strategy;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        if (!(type instanceof Class)) {
            return false;
        }

        if (!((Class<?>) type).isAssignableFrom(String.class)) {
            return false;
        }

        if (!(context.getSite() instanceof NamedStubbingSite)) {
            return false;
        }

        NamedStubbingSite namedSite = (NamedStubbingSite) context.getSite();
        WordSequence words = WordSequence.extractFrom(namedSite.getName());
        return strategy.accepts(words);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        NamedStubbingSite namedSite = (NamedStubbingSite) context.getSite();
        WordSequence words = WordSequence.extractFrom(namedSite.getName());
        return strategy.fake(faker, words, context);
    }

}
