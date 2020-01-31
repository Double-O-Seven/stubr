package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Executable;

/**
 * Stubbing site indicating that the current stubbing site is an {@link Executable}. Implementations of {@link
 * Executable} are {@link java.lang.reflect.Constructor} and {@link java.lang.reflect.Method}.
 */
public interface ExecutableStubbingSite extends StubbingSite {

    /**
     * Returns the {@link Executable} where the stubbing takes place.
     *
     * @return the executable
     */
    Executable getExecutable();

}
