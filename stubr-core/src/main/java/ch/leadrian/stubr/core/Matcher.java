package ch.leadrian.stubr.core;

@FunctionalInterface
public interface Matcher<T> {

    boolean matches(StubbingContext context, T value);

    default Matcher<T> and(Matcher<? super T> other) {
        return (context, value) -> this.matches(context, value) && other.matches(context, value);
    }

    default Matcher<T> or(Matcher<? super T> other) {
        return (context, value) -> this.matches(context, value) || other.matches(context, value);
    }

    default Matcher<T> negate() {
        return (context, value) -> !matches(context, value);
    }

}
