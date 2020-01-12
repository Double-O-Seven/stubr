package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

final class StubbingContextImpl implements StubbingContext {

    private static final EqualsAndHashCode<StubbingContextImpl> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(StubbingContextImpl.class)
            .compare(StubbingContextImpl::getStubber)
            .compare(StubbingContextImpl::getSite)
            .build();

    private final RootStubber stubber;
    private final StubbingSite site;

    StubbingContextImpl(RootStubber stubber, StubbingSite site) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        this.stubber = stubber;
        this.site = site;
    }

    @Override
    public RootStubber getStubber() {
        return stubber;
    }

    @Override
    public StubbingSite getSite() {
        return site;
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
