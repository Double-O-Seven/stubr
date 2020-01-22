package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.OptionalStubbingMode;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.nullable;
import static ch.leadrian.stubr.core.stubber.Stubbers.array;
import static ch.leadrian.stubr.core.stubber.Stubbers.commonConstantValues;
import static ch.leadrian.stubr.core.stubber.Stubbers.commonSuppliedValues;
import static ch.leadrian.stubr.core.stubber.Stubbers.defaultCollections;
import static ch.leadrian.stubr.core.stubber.Stubbers.defaultConstructor;
import static ch.leadrian.stubr.core.stubber.Stubbers.defaultValue;
import static ch.leadrian.stubr.core.stubber.Stubbers.emptyDefaultCollections;
import static ch.leadrian.stubr.core.stubber.Stubbers.enumValue;
import static ch.leadrian.stubr.core.stubber.Stubbers.factoryMethod;
import static ch.leadrian.stubr.core.stubber.Stubbers.nonDefaultConstructor;
import static ch.leadrian.stubr.core.stubber.Stubbers.nullValue;
import static ch.leadrian.stubr.core.stubber.Stubbers.optional;
import static ch.leadrian.stubr.core.stubber.Stubbers.proxy;
import static ch.leadrian.stubr.core.stubber.Stubbers.rootStubber;

public final class RootStubbers {

    private static final RootStubber DEFAULT = RootStubber.builder()
            .stubWith(defaultValue())
            .stubWith(enumValue())
            .stubWith(array(0))
            .stubWith(optional(OptionalStubbingMode.PRESENT))
            .stubWith(defaultCollections(1))
            .stubWith(defaultConstructor())
            .stubWith(nonDefaultConstructor())
            .stubWith(factoryMethod())
            .stubWith(proxy())
            .stubWith(rootStubber())
            .stubWith(commonConstantValues())
            .stubWith(commonSuppliedValues())
            .build();

    private static final RootStubber MINIMAL = RootStubber.builder()
            .include(getDefault())
            .stubWith(nullValue().when(annotatedSiteIs(nullable())))
            .stubWith(emptyDefaultCollections())
            .stubWith(optional(OptionalStubbingMode.EMPTY))
            .build();

    private RootStubbers() {
    }

    public static RootStubber getDefault() {
        return DEFAULT;
    }

    public static RootStubber minimal() {
        return MINIMAL;
    }

}
