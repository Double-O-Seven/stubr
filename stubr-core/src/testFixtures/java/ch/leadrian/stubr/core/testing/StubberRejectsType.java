package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberRejectsType implements StubberTest {

    private final Type acceptedType;

    StubberRejectsType(Type acceptedType) {
        requireNonNull(acceptedType, "stubber");
        this.acceptedType = acceptedType;
    }

    @Override
    public DynamicTest toDynamicTest(Stubber stubber, StubbingContext context) {
        String displayName = String.format("%s should rejects %s", stubber.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            boolean accepts = stubber.accepts(context, acceptedType);

            assertThat(accepts).isFalse();
        });
    }

}
