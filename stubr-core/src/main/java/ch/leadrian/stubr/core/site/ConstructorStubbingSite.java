package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;

/**
 * Stubbing site indicating that the current stubbing site is a constructor.
 */
public interface ConstructorStubbingSite extends StubbingSite {

    /**
     * Returns the constructor where the stubbing takes place.
     *
     * @return the constructor
     */
    Constructor<?> getConstructor();

}
