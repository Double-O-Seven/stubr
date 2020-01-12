package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

public final class MethodReturnValueStubbingSite implements MethodStubbingSite {

    private static final EqualsAndHashCode<MethodReturnValueStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodReturnValueStubbingSite.class)
            .compare(MethodReturnValueStubbingSite::getParent)
            .compare(MethodReturnValueStubbingSite::getMethod)
            .build();

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

    @Override
    public Method getMethod() {
        return method;
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
