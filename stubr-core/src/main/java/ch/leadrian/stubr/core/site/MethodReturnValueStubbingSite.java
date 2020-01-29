package ch.leadrian.stubr.core.site;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public final class MethodReturnValueStubbingSite implements MethodStubbingSite, AnnotatedStubbingSite {

    private static final EqualsAndHashCode<MethodReturnValueStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MethodReturnValueStubbingSite.class)
            .compareAndHash(MethodReturnValueStubbingSite::getParent)
            .compareAndHash(MethodReturnValueStubbingSite::getMethod)
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
    public AnnotatedElement getAnnotatedElement() {
        return getMethod();
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
                .toString();
    }

}
