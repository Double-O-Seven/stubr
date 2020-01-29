package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubbingStrategyRejectsType implements StubbingStrategyTest {

    private final Type acceptedType;

    StubbingStrategyRejectsType(Type acceptedType) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = String.format("%s should rejects %s", stubbingStrategy.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            boolean accepts = stubbingStrategy.accepts(context, acceptedType);

            assertThat(accepts).isFalse();
        });
    }

}
