package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

public enum TestStubbingSite implements StubbingSite {
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