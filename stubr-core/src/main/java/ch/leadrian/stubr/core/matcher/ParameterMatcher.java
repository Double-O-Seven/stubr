package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.ParameterStubbingSite;

import java.lang.reflect.Parameter;

import static java.util.Objects.requireNonNull;

final class ParameterMatcher<T> implements Matcher<T> {

    private final Matcher<? super Parameter> delegate;

    ParameterMatcher(Matcher<? super Parameter> matcher) {
        requireNonNull(matcher, "delegate");
        this.delegate = matcher;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        StubbingSite site = context.getSite();
        if (site instanceof ParameterStubbingSite) {
            Parameter parameter = ((ParameterStubbingSite) site).getParameter();
            return delegate.matches(context, parameter);
        }
        return false;
    }

}
