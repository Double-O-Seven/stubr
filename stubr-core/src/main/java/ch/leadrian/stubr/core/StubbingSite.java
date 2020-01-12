package ch.leadrian.stubr.core;

import java.util.Optional;

public interface StubbingSite {

    Optional<? extends StubbingSite> getParent();

}
