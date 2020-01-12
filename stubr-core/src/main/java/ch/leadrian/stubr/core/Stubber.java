package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.Stubbers;

import java.lang.reflect.Type;

public interface Stubber {

    boolean accepts(StubbingContext context, Type type);

    Object stub(StubbingContext context, Type type);

    default Stubber when(TypeMatcher typeMatcher) {
        return Stubbers.conditional(this, typeMatcher);
    }

}
