package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class ConstructorParameterStubbingSite implements StubbingSite {

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
}
