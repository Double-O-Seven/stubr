package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

public final class MethodParameterStubbingSite implements StubbingSite {

    private static final EqualsAndHashCode<MethodParameterStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodParameterStubbingSite.class)
            .compare(MethodParameterStubbingSite::getParent)
            .compare(MethodParameterStubbingSite::getMethod)
            .compare(MethodParameterStubbingSite::getParameter)
            .build();

    private final StubbingSite parent;
    private final Method method;
    private final Parameter parameter;

    MethodParameterStubbingSite(StubbingSite parent, Method method, Parameter parameter) {
        requireNonNull(parent, "parent");
        requireNonNull(method, "method");
        requireNonNull(parameter, "parameter");
        this.parent = parent;
        this.method = method;
        this.parameter = parameter;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public Method getMethod() {
        return method;
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
