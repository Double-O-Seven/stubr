package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;

import java.lang.reflect.Type;

final class NullValueStubber implements Stubber {

    static final NullValueStubber INSTANCE = new NullValueStubber();

    private NullValueStubber() {
    }

    @Override
    public boolean accepts(Type type) {
        return true;
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        return null;
    }
}
