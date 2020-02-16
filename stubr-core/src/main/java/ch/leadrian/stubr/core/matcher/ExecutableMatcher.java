package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.Matcher;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.ExecutableStubbingSite;

import java.lang.reflect.Executable;

import static java.util.Objects.requireNonNull;

final class ExecutableMatcher<T> implements Matcher<T> {

    private final Matcher<? super Executable> delegate;

    ExecutableMatcher(Matcher<? super Executable> delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean matches(StubbingContext context, T value) {
        StubbingSite site = context.getSite();
        if (site instanceof ExecutableStubbingSite) {
            Executable executable = ((ExecutableStubbingSite) site).getExecutable();
            return delegate.matches(context, executable);
        }
        return false;
    }

}
