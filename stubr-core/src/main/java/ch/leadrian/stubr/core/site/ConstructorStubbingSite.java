package ch.leadrian.stubr.core.site;

import java.lang.reflect.Constructor;

/**
 * Stubbing site indicating that the current stubbing site is a {@link Constructor}.
 */
public interface ConstructorStubbingSite extends ExecutableStubbingSite {

    /**
     * Returns the constructor where the stubbing takes place.
     *
     * @return the constructor
     */
    Constructor<?> getConstructor();

    /**
     * {@inheritDoc}
     */
    @Override
    default Constructor<?> getExecutable() {
        return getConstructor();
    }

}
