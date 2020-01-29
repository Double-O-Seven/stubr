package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyProvidesStub implements StubbingStrategyTest {

    private final Type acceptedType;
    private final Object expectedValue;

    StubbingStrategyProvidesStub(Type acceptedType, Object expectedValue) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
        this.expectedValue = expectedValue;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = String.format("%s should provide %s as stub for %s", stubbingStrategy.getClass().getSimpleName(), expectedValue, acceptedType);
        return dynamicTest(displayName, () -> {
            Object value = stubbingStrategy.stub(context, acceptedType);

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
