package ch.leadrian.stubr.mockito;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.AnnotatedStubbingSite;
import ch.leadrian.stubr.core.site.MethodStubbingSite;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

/**
 * Stubbing site indicating that the stubbing site is an {@link InvocationOnMock}.
 */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.of(parent);
    }

    /**
     * Returns the {@link InvocationOnMock}.
     *
     * @return the invocation
     */
    public InvocationOnMock getInvocation() {
        return invocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Method getMethod() {
        return invocation.getMethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Method getAnnotatedElement() {
        return getMethod();
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
                .add("invocation", invocation)
                .toString();
    }

}
