package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public final class MethodParameterStubbingSite implements ParameterStubbingSite, MethodStubbingSite {

    private static final EqualsAndHashCode<MethodParameterStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodParameterStubbingSite.class)
            .compareAndHash(MethodParameterStubbingSite::getParent)
            .compareAndHash(MethodParameterStubbingSite::getMethod)
            .compareAndHash(MethodParameterStubbingSite::getParameter)
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

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
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

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("parent", parent)
                .add("method", method)
                .add("parameter", parameter)
                .toString();
    }

}