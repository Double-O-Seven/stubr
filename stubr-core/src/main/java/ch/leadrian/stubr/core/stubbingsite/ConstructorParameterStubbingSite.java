package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

public final class ConstructorParameterStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<ConstructorParameterStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ConstructorParameterStubbingSite.class)
            .compare(ConstructorParameterStubbingSite::getParent)
            .compare(ConstructorParameterStubbingSite::getConstructor)
            .compare(ConstructorParameterStubbingSite::getParameter)
            .build();

    private final StubbingSite parent;
    private final Constructor<?> constructor;
    private final Parameter parameter;

    ConstructorParameterStubbingSite(StubbingSite parent, Constructor<?> constructor, Parameter parameter) {
        requireNonNull(parent, "parent");
        requireNonNull(constructor, "constructor");
        requireNonNull(parameter, "parameter");
        this.parent = parent;
        this.constructor = constructor;
        this.parameter = parameter;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }
}
