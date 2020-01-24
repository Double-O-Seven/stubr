package ch.leadrian.stubr.mockito;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.AnnotatedStubbingSite;
import ch.leadrian.stubr.core.stubbingsite.MethodStubbingSite;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

public final class MockitoStubbingSite implements MethodStubbingSite, AnnotatedStubbingSite {

    private static final EqualsAndHashCode<MockitoStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(MockitoStubbingSite.class)
            .compareAndHash(MockitoStubbingSite::getParent)
            .compareAndHash(MockitoStubbingSite::getInvocation)
            .build();

    private final StubbingSite parent;
    private final InvocationOnMock invocation;

    MockitoStubbingSite(StubbingSite parent, InvocationOnMock invocation) {
        requireNonNull(parent, "parent");
        requireNonNull(invocation, "invocation");
        this.parent = parent;
        this.invocation = invocation;
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    public InvocationOnMock getInvocation() {
        return invocation;
    }

    @Override
    public Method getMethod() {
        return invocation.getMethod();
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
}
