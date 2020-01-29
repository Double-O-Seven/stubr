package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;

import static ch.leadrian.stubr.core.type.Types.getRawType;

enum RootStubberStubbingStrategy implements StubbingStrategy {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getRawType(type)
                .filter(RootStubber.class::equals)
                .isPresent();
    }

    @Override
    public RootStubber stub(StubbingContext context, Type type) {
        return context.getStubber();
    }
}
