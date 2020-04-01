package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.ConstructorStubbingSite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ConstructorMatcherTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenConstructorStubbingSiteItShouldReturnTrueIfAndOnlyIfDelegateMatches(boolean delegateMatches) {
        StubbingContext context = StubbingContext.create(mock(Stubber.class), mock(ConstructorStubbingSite.class));
        Matcher<Object> matcher = Matchers.constructorIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isEqualTo(delegateMatches);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenNoConstructorStubbingSiteItShouldReturnFalse(boolean delegateMatches) {
        StubbingContext context = StubbingContext.create(mock(Stubber.class), mock(StubbingSite.class));
        Matcher<Object> matcher = Matchers.constructorIs((ctx, value) -> delegateMatches);

        boolean matches = matcher.matches(context, new Object());

        assertThat(matches)
                .isFalse();
    }

}