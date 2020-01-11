package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;

public final class ConstructorMatchers {

    private ConstructorMatchers() {
    }

    public static ConstructorMatcher any() {
        return constructor -> true;
    }

    public static ConstructorMatcher defaultConstructor() {
        return constructor -> constructor.getParameterCount() == 0;
    }

    public static ConstructorMatcher accepting(Class<?>... parameterTypes) {
        return new AcceptingConstructorMatcher(parameterTypes);
    }

}
