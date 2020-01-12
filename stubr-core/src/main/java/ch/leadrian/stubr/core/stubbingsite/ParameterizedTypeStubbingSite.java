package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public final class ParameterizedTypeStubbingSite implements StubbingSite {

    private final StubbingSite parent;
    private final ParameterizedType type;
    private final int parameterIndex;

    ParameterizedTypeStubbingSite(StubbingSite parent, ParameterizedType type, int parameterIndex) {
        this.parent = parent;
        this.type = type;
        this.parameterIndex = parameterIndex;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }
}
