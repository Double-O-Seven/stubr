package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;

import static java.util.Arrays.asList;

public final class ConstructorMatchers {

    public static final ConstructorMatcher ANY_MATCHER = constructor -> true;
    private static final ConstructorMatcher IS_DEFAULT_MATCHER = constructor -> constructor.getParameterCount() == 0;

    private ConstructorMatchers() {
    }

    public static ConstructorMatcher any() {
        return ANY_MATCHER;
    }

    public static ConstructorMatcher isDefault() {
        return IS_DEFAULT_MATCHER;
    }

    public static ConstructorMatcher accepting(Class<?>... parameterTypes) {
        return new AcceptingConstructorMatcher(asList(parameterTypes));
    }

}
