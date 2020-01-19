package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public final class ArrayStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<ArrayStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ArrayStubbingSite.class)
            .compareAndHash(ArrayStubbingSite::getParent)
            .compareAndHash(ArrayStubbingSite::getComponentType)
            .build();

    private final StubbingSite parent;
    private final Class<?> componentType;

    ArrayStubbingSite(StubbingSite parent, Class<?> componentType) {
        requireNonNull(parent, "parent");
        requireNonNull(componentType, "type");
        this.parent = parent;
        this.componentType = componentType;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public Class<?> getComponentType() {
        return componentType;
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
                .add("componentType", componentType)
                .toString();
    }
}
