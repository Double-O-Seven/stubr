package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.ConstructorStubbingSite;

import java.lang.reflect.Constructor;

import static java.util.Objects.requireNonNull;

final class ConstructorMatcher<T> implements Matcher<T> {

    private final Matcher<? super Constructor<?>> delegate;

    ConstructorMatcher(Matcher<? super Constructor<?>> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        StubbingSite site = context.getSite();
        if (site instanceof ConstructorStubbingSite) {
            Constructor<?> constructor = ((ConstructorStubbingSite) site).getConstructor();
            return delegate.matches(context, constructor);
        }
        return false;
    }

}
