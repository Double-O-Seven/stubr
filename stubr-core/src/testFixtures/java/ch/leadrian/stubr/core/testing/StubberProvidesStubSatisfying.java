package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.DynamicTest;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class StubberProvidesStubSatisfying implements StubberTest {

    private final Type acceptedType;
    private final Consumer<Object> assertion;

    StubberProvidesStubSatisfying(Type acceptedType, Consumer<Object> assertion) {
        requireNonNull(acceptedType, "stubber");
        this.acceptedType = acceptedType;
        this.assertion = assertion;
    }

    @Override
    public DynamicTest toDynamicTest(Stubber stubber, StubbingContext context) {
        String displayName = String.format("%s should provide stub for %s", stubber.getClass().getSimpleName(), acceptedType);
        return dynamicTest(displayName, () -> {
            Object value = stubber.stub(context, acceptedType);

            assertion.accept(value);
        });
    }
}
