package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.MethodStubbingSite;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNull;

final class MethodMatcher<T> implements Matcher<T> {

    private final Matcher<? super Method> delegate;

    MethodMatcher(Matcher<? super Method> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        StubbingSite site = context.getSite();
        if (site instanceof MethodStubbingSite) {
            Method method = ((MethodStubbingSite) site).getMethod();
            return delegate.matches(context, method);
        }
        return false;
    }

}
