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

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StubbingSiteTest {

    @Test
    void givenParentItShouldReturnSitesUpToRoot() {
        StubbingSite parentOfParent = new TestStubbingSite();
        StubbingSite parent = new TestStubbingSite(parentOfParent);
        StubbingSite stubbingSite = new TestStubbingSite(parent);

        Stream<StubbingSite> stubbingSites = stubbingSite.walk();

        assertThat(stubbingSites)
                .containsExactly(stubbingSite, parent, parentOfParent);
    }

    @Test
    void givenNoParentItShouldReturnStreamOfSelf() {
        StubbingSite stubbingSite = new TestStubbingSite();

        Stream<StubbingSite> stubbingSites = stubbingSite.walk();

        assertThat(stubbingSites)
                .containsExactly(stubbingSite);
    }

    private static final class TestStubbingSite implements StubbingSite {

        private final StubbingSite parent;

        private TestStubbingSite(StubbingSite parent) {
            this.parent = parent;
        }

        private TestStubbingSite() {
            this.parent = null;
        }

        @Override
        public Optional<? extends StubbingSite> getParent() {
            return Optional.ofNullable(parent);
        }

    }

}