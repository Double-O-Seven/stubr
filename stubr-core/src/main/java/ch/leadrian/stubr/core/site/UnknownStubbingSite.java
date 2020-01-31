package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

/**
 * A {@link StubbingSite} indicating that the current stubbing site is not known.
 * <p>
 * By default, a {@link ch.leadrian.stubr.core.Stubber} uses this site if none other is given.
 */
public final class UnknownStubbingSite implements StubbingSite {

    static final UnknownStubbingSite INSTANCE = new UnknownStubbingSite();

    private UnknownStubbingSite() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<? extends StubbingSite> getParent() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
