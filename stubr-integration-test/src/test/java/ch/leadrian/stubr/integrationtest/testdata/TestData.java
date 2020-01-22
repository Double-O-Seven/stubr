package ch.leadrian.stubr.integrationtest.testdata;

public final class TestData {

    private final Arrays arrays;
    private final Collections collections;
    private final CommonDefaults commonDefaults;
    private final Primitives primitives;
    private final PrimitiveWrappers primitiveWrappers;

    public TestData(Arrays arrays, Collections collections, CommonDefaults commonDefaults, Primitives primitives, PrimitiveWrappers primitiveWrappers) {
        this.arrays = arrays;
        this.collections = collections;
        this.commonDefaults = commonDefaults;
        this.primitives = primitives;
        this.primitiveWrappers = primitiveWrappers;
    }

    public Arrays getArrays() {
        return arrays;
    }

    public Collections getCollections() {
        return collections;
    }

    public CommonDefaults getCommonDefaults() {
        return commonDefaults;
    }

    public Primitives getPrimitives() {
        return primitives;
    }

    public PrimitiveWrappers getPrimitiveWrappers() {
        return primitiveWrappers;
    }
}
