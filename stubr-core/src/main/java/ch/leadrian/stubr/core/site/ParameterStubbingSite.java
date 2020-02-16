package ch.leadrian.stubr.core.site;

import java.lang.reflect.Parameter;

/**
 * Stubbing site indicating that the current stubbing site is a {@link Parameter}.
 */
public interface ParameterStubbingSite extends AnnotatedStubbingSite {

    /**
     * Returns the parameter where the stubbing takes place.
     *
     * @return the parameter
     */
    Parameter getParameter();

    /**
     * Returns the index of parameter where the stubbing takes place.
     *
     * @return the parameter index
     */
    int getParameterIndex();

    /**
     * {@inheritDoc}
     */
    @Override
    default Parameter getAnnotatedElement() {
        return getParameter();
    }

}
