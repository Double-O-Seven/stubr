package ch.leadrian.stubr.samples;

import ch.leadrian.stubr.junit.Stubr;
import ch.leadrian.stubr.junit.annotation.Include;
import ch.leadrian.stubr.junit.annotation.Stub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test example showing how to test a test subject ({@link FamilyService} whose dependencies are mocked.
 * <p>
 * {@link FamilyStubs} provides a stubbing strategy that provides a stub for a {@link TestSubject} annotated test method
 * parameter using a suitable default strategy (in this case using a constructor) and then mocking all values that are
 * required to instantiate the stub.
 * <p>
 * The provided mocks are memoized such that for each type there is exactly one mock. {@link FamilyStubs} has been
 * configured to also provide the memoized mock for any test method parameter annotated with {@link MockedDependency}.
 *
 * @see FamilyStubs
 */
@ExtendWith(Stubr.class)
@Include(FamilyStubs.class)
class FamilyServiceTest {

    @Test
    void shouldCreateFamily(
            @TestSubject FamilyService familyService,
            @MockedDependency FamilyFactory familyFactory,
            @Stub Person mom,
            @Stub Person dad,
            @Stub Family expectedFamily
    ) {
        when(familyFactory.create(mom, dad)).thenReturn(expectedFamily);

        Family family = familyService.performConservativeMarriage(mom, dad);

        assertThat(family)
                .isSameAs(expectedFamily);
    }

}
