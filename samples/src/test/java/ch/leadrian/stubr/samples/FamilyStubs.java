package ch.leadrian.stubr.samples;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.junit.StubberProvider;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Parameter;
import java.util.List;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.annotatedWith;
import static ch.leadrian.stubr.core.matcher.Matchers.parameterIs;
import static ch.leadrian.stubr.core.matcher.Matchers.parent;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.memoized;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.suppliedValue;
import static ch.leadrian.stubr.mockito.MockitoStubbingStrategies.mock;
import static java.util.Collections.singletonList;

public final class FamilyStubs implements StubberProvider {

    @Override
    public List<? extends Stubber> getStubbers(ExtensionContext extensionContext) {
        Stubber stubber = Stubber.builder()
                .stubWith(suppliedValue(String.class, sequenceNumber -> "First Name " + sequenceNumber).when(parameterIs(named("firstName"))))
                .stubWith(suppliedValue(String.class, sequenceNumber -> "Last Name " + sequenceNumber).when(parameterIs(named("lastName"))))
                .stubWith(memoized(mock()).when(parent(annotatedSiteIs(annotatedWith(TestSubject.class))).or(annotatedSiteIs(annotatedWith(MockedDependency.class)))))
                .build();
        return singletonList(stubber);
    }

    /*
     * Kotlin compile option javaParameters has been set to true, so the parameter names will be retained.
     */
    private static Matcher<Parameter> named(String name) {
        return (context, parameter) -> parameter.getName().equals(name);
    }

}
