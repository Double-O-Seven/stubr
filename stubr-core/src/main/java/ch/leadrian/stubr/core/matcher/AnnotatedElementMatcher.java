package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.AnnotatedStubbingSite;

import java.lang.reflect.AnnotatedElement;

import static java.util.Objects.requireNonNull;

final class AnnotatedElementMatcher<T> implements Matcher<T> {

    private final Matcher<? super AnnotatedElement> delegate;

    AnnotatedElementMatcher(Matcher<? super AnnotatedElement> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        StubbingSite site = context.getSite();
        if (site instanceof AnnotatedStubbingSite) {
            AnnotatedElement annotatedElement = ((AnnotatedStubbingSite) site).getAnnotatedElement();
            return delegate.matches(context, annotatedElement);
        }
        return false;
    }

}
