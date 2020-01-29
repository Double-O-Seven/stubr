package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.lang.reflect.Type;

public interface StubbingStrategy {

    boolean accepts(StubbingContext context, Type type);

    Object stub(StubbingContext context, Type type);

    default StubbingStrategy when(Matcher<? super Type> typeMatcher) {
        return StubbingStrategies.conditional(this, typeMatcher);
    }

}
