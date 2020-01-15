package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UnknownStubbingSiteTest {

    @Test
    void shouldReturnEmptyAsParent() {
        Optional<? extends StubbingSite> parent = StubbingSites.unknown().getParent();

        assertThat(parent)
                .isEmpty();
    }

    @Test
    void toStringShouldReturnClassName() {
        String string = StubbingSites.unknown().toString();

        assertThat(string)
                .isEqualTo("UnknownStubbingSite");
    }

}