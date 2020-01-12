package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class MethodReturnValueStubbingSite implements StubbingSite {

    private final StubbingSite parent;
    private final Method method;

    MethodReturnValueStubbingSite(StubbingSite parent, Method method) {
        requireNonNull(parent, "parent");
        requireNonNull(method, "method");
        this.parent = parent;
        this.method = method;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public Method getMethod() {
        return method;
    }

}
