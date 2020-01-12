package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public final class MethodParameterStubbingSite implements StubbingSite {

    private final StubbingSite parent;
    private final Method method;
    private final Parameter parameter;

    MethodParameterStubbingSite(StubbingSite parent, Method method, Parameter parameter) {
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
}
