package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberProvidesStub implements StubberTest {

    private final Type acceptedType;
    private final Object expectedValue;

    StubberProvidesStub(Type acceptedType, Object expectedValue) {
        requireNonNull(acceptedType, "stubber");
        this.acceptedType = acceptedType;
        this.expectedValue = expectedValue;
    }

    @Override
    public DynamicTest toDynamicTest(Stubber stubber, StubbingContext context) {
        String displayName = String.format("%s should provide %s as stub for %s", stubber.getClass().getSimpleName(), expectedValue, acceptedType);
        return dynamicTest(displayName, () -> {
            Object value = stubber.stub(context, acceptedType);

            if (expectedValue == null) {
                assertThat(value).isNull();
            } else {
                assertThat(value)
                        .hasSameClassAs(expectedValue)
                        .isEqualTo(expectedValue);
            }
        });
    }
}
