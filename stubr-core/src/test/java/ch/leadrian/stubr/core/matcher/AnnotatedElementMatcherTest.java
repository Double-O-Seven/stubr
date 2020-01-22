package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.AnnotatedStubbingSite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AnnotatedElementMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenAnnotatedElementStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
        StubbingContext context = new StubbingContext(mock(RootStubber.class), mock(AnnotatedStubbingSite.class));
        Matcher<Object> matcher = Matchers.annotatedSiteIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isEqualTo(delegateMatches);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoAnnotatedElementStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
        StubbingContext context = new StubbingContext(mock(RootStubber.class), mock(StubbingSite.class));
        Matcher<Object> matcher = Matchers.annotatedSiteIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isFalse();
    }

}