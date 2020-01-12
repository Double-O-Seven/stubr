package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

public final class UnknownStubbingSite implements StubbingSite {

    static final UnknownStubbingSite INSTANCE = new UnknownStubbingSite();

    private UnknownStubbingSite() {
    }

    @Override
    public Optional<? extends StubbingSite> getParent() {
        return Optional.empty();
    }
}
