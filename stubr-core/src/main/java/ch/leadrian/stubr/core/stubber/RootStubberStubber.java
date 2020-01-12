package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.util.Types.getActualClass;

enum RootStubberStubber implements Stubber {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getActualClass(type)
                .filter(RootStubber.class::equals)
                .isPresent();
    }

    @Override
    public RootStubber stub(StubbingContext context, Type type) {
        return context.getStubber();
    }
}
