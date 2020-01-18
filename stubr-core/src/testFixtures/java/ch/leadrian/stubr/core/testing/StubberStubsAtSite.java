package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberStubsAtSite implements StubberTest {

    private final Type acceptedType;
    private final List<StubbingSite> expectedSites;

    StubberStubsAtSite(Type acceptedType, List<StubbingSite> expectedSites) {
        requireNonNull(acceptedType, "acceptedType");
        requireNonNull(expectedSites, "expectedSites");
        this.acceptedType = acceptedType;
        this.expectedSites = new ArrayList<>(expectedSites);
    }

    @Override
    public DynamicTest toDynamicTest(Stubber stubber, StubbingContext context) {
        String displayName = getDisplayName(stubber);
        return dynamicTest(displayName, () -> {
            CapturingRootStubber capturingRootStubber = new CapturingRootStubber(context.getStubber());
            StubbingContext capturingContext = new StubbingContext(capturingRootStubber, context.getSite());

            stubber.stub(capturingContext, acceptedType);

            assertThat(capturingRootStubber.getCapturedSites())
                    .containsExactlyElementsOf(expectedSites);
        });
    }

    private String getDisplayName(Stubber stubber) {
        String sites = expectedSites
                .stream()
                .map(Object::toString)
                .collect(joining(", "));
        return String.format("%s should stub %s at %s", stubber.getClass().getSimpleName(), acceptedType, sites);
    }

    static final class CapturingRootStubber extends RootStubber {

        private final List<StubbingSite> capturedSites = new ArrayList<>();
        private final RootStubber delegate;

        CapturingRootStubber(RootStubber delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Result<?> tryToStub(Type type, StubbingContext context) {
            StubbingSite site = context.getSite();
            capturedSites.add(site);
            return delegate.tryToStub(type, site);
        }

        public List<StubbingSite> getCapturedSites() {
            return capturedSites;
        }
    }
}