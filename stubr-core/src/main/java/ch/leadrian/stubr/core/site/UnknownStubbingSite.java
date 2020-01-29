package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

public enum UnknownStubbingSite implements StubbingSite {
    INSTANCE;

    @Override
    public Optional<? extends StubbingSite> getParent() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
