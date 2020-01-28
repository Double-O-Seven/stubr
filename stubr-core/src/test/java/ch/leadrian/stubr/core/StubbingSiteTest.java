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