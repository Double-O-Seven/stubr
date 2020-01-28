package ch.leadrian.stubr.integrationtest.testdata;

public final class TestData {

    private final Annotations annotations;
    private final Arrays arrays;
    private final Collections collections;
    private final Primitives primitives;
    private final PrimitiveWrappers primitiveWrappers;

    public TestData(Annotations annotations, Arrays arrays, Collections collections, Primitives primitives, PrimitiveWrappers primitiveWrappers) {
        this.annotations = annotations;
        this.arrays = arrays;
        this.collections = collections;
        this.primitives = primitives;
        this.primitiveWrappers = primitiveWrappers;
    }

    public Annotations getAnnotations() {
        return annotations;
    }

    public Arrays getArrays() {
        return arrays;
    }

    public Collections getCollections() {
        return collections;
    }

    public Primitives getPrimitives() {
        return primitives;
    }

    public PrimitiveWrappers getPrimitiveWrappers() {
        return primitiveWrappers;
    }

}
