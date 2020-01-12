package ch.leadrian.stubr.core;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;
import static java.util.Objects.requireNonNull;

public final class StubbingContext {

    private static final EqualsAndHashCode<StubbingContext> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(StubbingContext.class)
            .compare(StubbingContext::getStubber)
            .compare(StubbingContext::getSite)
            .build();

    private final RootStubber stubber;
    private final StubbingSite site;

    public StubbingContext(RootStubber stubber, StubbingSite site) {
        requireNonNull(stubber, "stubber");
        requireNonNull(site, "site");
        this.stubber = stubber;
        this.site = site;
    }

    public RootStubber getStubber() {
        return stubber;
    }

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
