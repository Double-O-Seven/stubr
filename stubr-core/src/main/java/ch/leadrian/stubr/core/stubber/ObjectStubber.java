package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;

import java.lang.reflect.Type;

final class ObjectStubber implements Stubber {

    static final ObjectStubber INSTANCE = new ObjectStubber();

    private ObjectStubber() {
    }

    @Override
    public boolean accepts(Type type) {
        return Object.class == type;
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        return new Object();
    }
}
