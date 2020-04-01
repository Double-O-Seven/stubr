package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParentMatcherTest {

    @Test
    void givenNoParentSiteItShouldNotMatch() {
        StubbingSite site = new TestStubbingSite(null);
        StubbingContext context = StubbingContext.create(mock(Stubber.class), site);
        Matcher<String> matcher = Matchers.parent((c, v) -> true);

        boolean matches = matcher.matches(context, "test");

        assertThat(matches)
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenParentItShouldDelegateMatching(boolean expectedMatch) {
        StubbingSite parentSite = new TestStubbingSite(null);
        StubbingSite site = new TestStubbingSite(parentSite);
        Stubber stubber = mock(Stubber.class);
        StubbingContext context = StubbingContext.create(stubber, site);
        @SuppressWarnings("unchecked")
        Matcher<String> delegate = mock(Matcher.class);
        when(delegate.matches(any(), any()))
                .thenReturn(expectedMatch);
        Matcher<String> matcher = Matchers.parent(delegate);

        boolean matches = matcher.matches(context, "test");

        assertAll(
                () -> assertThat(matches).isEqualTo(expectedMatch),
                () -> verify(delegate).matches(StubbingContext.create(stubber, parentSite), "test")
        );
    }

    private static class TestStubbingSite implements StubbingSite {

        private final StubbingSite parent;

        private TestStubbingSite(StubbingSite parent) {
            this.parent = parent;
        }

        @Override
        public Optional<StubbingSite> getParent() {
            return Optional.ofNullable(parent);
        }

    }

}