package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

final class NullValueStubber implements Stubber {

    static final NullValueStubber INSTANCE = new NullValueStubber();

    private NullValueStubber() {
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return true;
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return null;
    }
}
