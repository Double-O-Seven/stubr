package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubber.OptionalStubbingMode;

import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.nullable;
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

    private static final RootStubber DEFAULT_ROOT_STUBBER = RootStubber.builder()
            .stubWith(defaultValue())
            .stubWith(proxy())
            .stubWith(enumValue())
            .stubWith(optional(OptionalStubbingMode.PRESENT))
            .stubWith(defaultConstructor())
            .stubWith(nonDefaultConstructor())
            .stubWith(factoryMethod())
            .stubWith(defaultCollections(1))
            .stubWith(rootStubber())
            .stubWith(commonConstantValues())
            .stubWith(commonSuppliedValues())
            .build();

    private static final RootStubber MINIMAL_ROOT_STUBBER = RootStubber.builder()
            .include(defaultRootStubber())
            .stubWith(nullValue().when(annotatedSiteIs(nullable())))
            .stubWith(optional(OptionalStubbingMode.EMPTY))
            .stubWith(emptyDefaultCollections())
            .build();

    private RootStubbers() {
    }

    public static RootStubber defaultRootStubber() {
        return DEFAULT_ROOT_STUBBER;
    }

    public static RootStubber minimalRootStubber() {
        return MINIMAL_ROOT_STUBBER;
    }

}
