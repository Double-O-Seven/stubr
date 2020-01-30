package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

/**
 * This class is a container for contextual information during a stubbing process.
 * <p>
 * Included are the {@link Stubber} performing the stubbing as well as the {@link StubbingSite} which indicates where
 * the requested stub value will be used.
 */
public final class StubbingContext {

    private static final EqualsAndHashCode<StubbingContext> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(StubbingContext.class)
            .compareAndHash(StubbingContext::getStubber)
            .compareAndHash(StubbingContext::getSite)
            .build();

    private final Stubber stubber;
    private final StubbingSite site;

    /**
     * Under normal circumstances it is not required to instantiate a {@link StubbingContext} manually, as {@link
     * Stubber}s will automatically create a suitable instance during the stubbing process.
     * <p>
     * However, a custom implementation of {@link Stubber} might override the {@link StubbingContext} in order to
     * enhance the given {@link Stubber} or {@link StubbingSite}.
     *
     * @param stubber the {@link Stubber} performing the stubbing
     * @param site    the {@link StubbingSite} where the requested stub value will be used
     */
    public StubbingContext(Stubber stubber, StubbingSite site) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        this.stubber = stubber;
        this.site = site;
    }

    /**
     * @return the {@link Stubber} performing the stubbing
     */
    public Stubber getStubber() {
        return stubber;
    }

    /**
     * @return the {@link StubbingSite} where the requested stub value will be used
     */
    public StubbingSite getSite() {
        return site;
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

}
