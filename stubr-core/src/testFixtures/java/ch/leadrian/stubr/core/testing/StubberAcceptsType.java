package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberAcceptsType implements StubberTest {

    private final Type acceptedType;

    StubberAcceptsType(Type acceptedType) {
        requireNonNull(acceptedType, "acceptedType");
        this.acceptedType = acceptedType;
    }

    @Override
    public DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context) {
        String displayName = String.format("%s should accept %s", stubbingStrategy.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            boolean accepts = stubbingStrategy.accepts(context, acceptedType);

            assertThat(accepts).isTrue();
        });
    }

}
