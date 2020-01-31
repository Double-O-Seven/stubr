package ch.leadrian.stubr.core.site;

import java.lang.reflect.Method;

/**
 * Stubbing site indicating that the current stubbing site is a {@link Method}.
 */
public interface MethodStubbingSite extends ExecutableStubbingSite {

    /**
     * Returns the method where the stubbing takes place.
     *
     * @return the method
     */
    Method getMethod();

    /**
     * {@inheritDoc}
     */
    @Override
    default Method getExecutable() {
        return getMethod();
    }

}
