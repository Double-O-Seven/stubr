package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.MethodStubbingSite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MethodMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenMethodStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
        StubbingContext context = new StubbingContext(mock(RootStubber.class), mock(MethodStubbingSite.class));
        Matcher<Object> matcher = Matchers.methodIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isEqualTo(delegateMatches);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoMethodStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
        StubbingContext context = new StubbingContext(mock(RootStubber.class), mock(StubbingSite.class));
        Matcher<Object> matcher = Matchers.methodIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isFalse();
    }

}