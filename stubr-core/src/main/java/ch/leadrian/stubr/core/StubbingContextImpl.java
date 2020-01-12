package ch.leadrian.stubr.core;

final class StubbingContextImpl implements StubbingContext {

    private final RootStubber rootStubber;
    private final StubbingSite site;

    StubbingContextImpl(RootStubber rootStubber, StubbingSite site) {
        this.rootStubber = rootStubber;
        this.site = site;
    }

    @Override
    public RootStubber getStubber() {
        return rootStubber;
    }

    @Override
    public StubbingSite getSite() {
        return site;
    }
}
