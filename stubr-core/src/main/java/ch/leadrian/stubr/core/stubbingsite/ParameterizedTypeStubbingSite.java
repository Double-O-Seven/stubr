package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public final class ParameterizedTypeStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<ParameterizedTypeStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ParameterizedTypeStubbingSite.class)
            .compareAndHash(ParameterizedTypeStubbingSite::getParent)
            .compareAndHash(ParameterizedTypeStubbingSite::getType)
            .compareAndHashPrimitive(ParameterizedTypeStubbingSite::getParameterIndex)
            .build();

    private final StubbingSite parent;
    private final ParameterizedType type;
    private final int parameterIndex;

    ParameterizedTypeStubbingSite(StubbingSite parent, ParameterizedType type, int parameterIndex) {
        requireNonNull(parent, "parent");
        requireNonNull(type, "type");
        this.parent = parent;
        this.type = type;
        this.parameterIndex = parameterIndex;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public ParameterizedType getType() {
        return type;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("parent", parent)
                .add("type", type)
                .add("parameterIndex", parameterIndex)
                .toString();
    }
}
