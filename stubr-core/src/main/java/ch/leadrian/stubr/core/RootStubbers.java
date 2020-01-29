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

/**
 * Class containing factory methods for basic {@link RootStubber}s.
 */
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

    /**
     * Provides a stateless {@link RootStubber} that will stub the following stub values:
     * <ul>
     * <li>Default values for primitives and their wrappers</li>
     * <li>{@link java.lang.reflect.Proxy} instances for interfaces that return stub values for non-void method
     * calls</li>
     * <li>Enum values</li>
     * <li>Non-empty {@link java.util.Optional}s</li>
     * <li>Object that can be created using a default constructor</li>
     * <li>Object that can be created using a non-default constructor</li>
     * <li>Object that can be created using a factory method</li>
     * <li>Common collections and array, filled with a single element</li>
     * <li>Reasonable constant values for commonly used immutable classes such as {@link java.time.LocalDate}</li>
     * <li>Reasonable constant values for commonly used mutable classes such as {@link java.util.Date}</li>
     * </ul>
     *
     * @return a {@link RootStubber} stubbing non-null, non-empty default values.
     * @see ch.leadrian.stubr.core.stubber.Stubbers
     */
    public static RootStubber defaultRootStubber() {
        return DEFAULT_ROOT_STUBBER;
    }

    /**
     * Provides a stateless {@link RootStubber} that uses {@link RootStubbers#defaultRootStubber()} as a baseline.
     * However, default collections, {@link java.util.Optional}s and arrays are kept empty and nullable sites will
     * receive {@code null} as stub value.
     *
     * @return a {@link RootStubber} stubbing nullable, empty default values.
     * @see ch.leadrian.stubr.core.stubber.Stubbers
     */
    public static RootStubber minimalRootStubber() {
        return MINIMAL_ROOT_STUBBER;
    }

}
