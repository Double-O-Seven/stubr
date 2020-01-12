package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

final class ObjectStubber implements Stubber {

    static final ObjectStubber INSTANCE = new ObjectStubber();

    private ObjectStubber() {
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return Object.class == type;
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return new Object();
    }
}
