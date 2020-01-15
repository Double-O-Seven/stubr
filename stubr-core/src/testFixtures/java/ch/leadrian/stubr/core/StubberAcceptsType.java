package ch.leadrian.stubr.core;

import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberAcceptsType implements StubberTest {

    private final Type acceptedType;

    StubberAcceptsType(Type acceptedType) {
        requireNonNull(acceptedType, "stubber");
        this.acceptedType = acceptedType;
    }

    @Override
    public DynamicTest toDynamicTest(Stubber stubber, StubbingContext context) {
        String displayName = String.format("%s should accept %s", stubber.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            boolean accepts = stubber.accepts(context, acceptedType);

            assertThat(accepts).isTrue();
        });
    }
}
