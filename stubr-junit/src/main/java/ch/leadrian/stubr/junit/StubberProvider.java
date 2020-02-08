package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

/**
 * A class used by {@link ch.leadrian.stubr.junit.annotation.Include} to determine which {@link Stubber}s should be
 * included when configuring a {@link Stubber} for a test case.
 * <p>
 * Any implementation must provide a default constructor since all instances are instantiated through reflection.
 *
 * @see ch.leadrian.stubr.junit.annotation.Include
 */
public interface StubberProvider {

    /**
     * Returns a list of {@link Stubber}s that should be included when configuring a {@link Stubber} for a test case.
     * <p>
     * The {@link Stubber}s will be included in the {@link Stubber} in the order in which they are present in the given
     * list.
     *
     * @param extensionContext the current extension context
     * @return a list of {@link Stubber}s that should be included when configuring a {@link Stubber} for a test case
     * @see ch.leadrian.stubr.core.StubberBuilder#include(Stubber)
     */
    List<? extends Stubber> getStubbers(ExtensionContext extensionContext);

}
