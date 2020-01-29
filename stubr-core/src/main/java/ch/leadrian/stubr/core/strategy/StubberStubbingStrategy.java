package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.getRawType;

enum StubberStubbingStrategy implements StubbingStrategy {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(Stubber.class::equals)
                .isPresent();
    }

    @Override
    public Stubber stub(StubbingContext context, Type type) {
        return context.getStubber();
    }
}
