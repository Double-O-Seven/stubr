package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * Stubbing site indicating that the current stubbing site is a constructor parameter.
 */
public final class ConstructorParameterStubbingSite implements ConstructorStubbingSite, ParameterStubbingSite {

    private static final EqualsAndHashCode<ConstructorParameterStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ConstructorParameterStubbingSite.class)
            .compareAndHash(ConstructorParameterStubbingSite::getParent)
            .compareAndHash(ConstructorParameterStubbingSite::getConstructor)
            .compareAndHash(ConstructorParameterStubbingSite::getParameter)
            .compareAndHashPrimitive(ConstructorParameterStubbingSite::getParameterIndex)
            .build();

    private final StubbingSite parent;
    private final Constructor<?> constructor;
    private final Parameter parameter;
    private final int parameterIndex;

    ConstructorParameterStubbingSite(StubbingSite parent, Constructor<?> constructor, Parameter parameter, int parameterIndex) {
        requireNonNull(parent, "parent");
        requireNonNull(constructor, "constructor");
        requireNonNull(parameter, "parameter");
        this.parent = parent;
        this.constructor = constructor;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getParameterIndex() {
        return parameterIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("parent", parent)
                .add("constructor", constructor)
                .add("parameter", parameter)
                .toString();
    }

}
