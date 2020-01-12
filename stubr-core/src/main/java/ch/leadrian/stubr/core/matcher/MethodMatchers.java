package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.MethodMatcher;

public final class MethodMatchers {

    private MethodMatchers() {
    }

    public static MethodMatcher any() {
        return method -> true;
    }
}
