package ch.leadrian.stubr.core;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class StubbingContextTest {

    @Test
    void shouldCreateContextWithArguments() {
        Stubber stubber = mock(Stubber.class);
        StubbingSite site = mock(StubbingSite.class);

        StubbingContext context = new StubbingContext(stubber, site);

        assertAll(
                () -> assertThat(context.getStubber()).isEqualTo(stubber),
                () -> assertThat(context.getSite()).isEqualTo(site)
        );
    }

    @Test
    void testEquals() {
        Stubber stubber1 = mock(Stubber.class);
        StubbingSite site1 = mock(StubbingSite.class);
        Stubber stubber2 = mock(Stubber.class);
        StubbingSite site2 = mock(StubbingSite.class);

        new EqualsTester()
                .addEqualityGroup(new StubbingContext(stubber1, site1), new StubbingContext(stubber1, site1))
                .addEqualityGroup(new StubbingContext(stubber2, site2), new StubbingContext(stubber2, site2))
                .addEqualityGroup("Test")
                .testEquals();
    }

}