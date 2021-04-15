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

package ch.leadrian.stubr.integrationtest.testdata;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static ch.leadrian.stubr.internal.com.google.common.base.MoreObjects.toStringHelper;

public class Cherry {

    private static final EqualsAndHashCode<Cherry> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Cherry.class)
            .compareAndHash(Cherry::getColor)
            .compareAndHash(Cherry::getWeight)
            .build();

    private final Color color;
    private final int weight;

    public Cherry(Color color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        return EQUALS_AND_HASH_CODE.equals(this, o);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("color", color)
                .add("weight", weight)
                .toString();
    }

    public enum Color {
        RED,
        BLACK
    }

}
