package ch.leadrian.stubr.core;

import java.util.Optional;
import java.util.stream.Stream;

public interface StubbingSite {

    Optional<? extends StubbingSite> getParent();

    default Stream<StubbingSite> walk() {
        return getParent()
                .map(parent -> Stream.concat(Stream.of(this), parent.walk()))
                .orElseGet(() -> Stream.of(this));
    }

}
