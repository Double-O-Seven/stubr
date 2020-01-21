package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;

public final class Matchers {

    private Matchers() {
    }

    public static <T> Matcher<T> any() {
        return (context, value) -> true;
    }
}
