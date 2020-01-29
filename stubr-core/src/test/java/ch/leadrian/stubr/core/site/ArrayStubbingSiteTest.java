package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class ArrayStubbingSiteTest {

    @Test
    void shouldReturnParent() {
        StubbingSite expectedParent = mock(StubbingSite.class);
        ArrayStubbingSite site = StubbingSites.array(expectedParent, String.class);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnComponentType() {
        ArrayStubbingSite site = StubbingSites.array(mock(StubbingSite.class), String.class);

        Object componentType = site.getComponentType();

        assertThat(componentType)
                .isEqualTo(String.class);
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() {
        StubbingSite parent1 = mock(StubbingSite.class);
        StubbingSite parent2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(StubbingSites.array(parent1, String.class), StubbingSites.array(parent1, String.class))
                .addEqualityGroup(StubbingSites.array(parent1, Integer.class), StubbingSites.array(parent1, Integer.class))
                .addEqualityGroup(StubbingSites.array(parent2, String.class), StubbingSites.array(parent2, String.class))
                .addEqualityGroup(StubbingSites.array(parent2, Integer.class), StubbingSites.array(parent2, Integer.class))
                .testEquals();
    }

}