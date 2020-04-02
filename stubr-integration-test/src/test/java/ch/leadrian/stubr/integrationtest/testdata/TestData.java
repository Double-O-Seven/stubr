/*
 *
 *  * Copyright (C) 2020 Adrian-Philipp Leuenberger
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
