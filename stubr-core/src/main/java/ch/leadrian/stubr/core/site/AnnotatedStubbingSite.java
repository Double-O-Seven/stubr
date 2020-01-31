package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.AnnotatedElement;

/**
 * Stubbing site with an {@link AnnotatedElement}.
 *
 * @see ConstructorParameterStubbingSite
 * @see MethodParameterStubbingSite
 * @see MethodReturnValueStubbingSite
 */
public interface AnnotatedStubbingSite extends StubbingSite {

    /**
     * Returns the {@link AnnotatedElement} at the stubbing site.
     * <p>
     * This might be the {@link java.lang.reflect.Parameter} of a {@link java.lang.reflect.Constructor} or {@link
     * java.lang.reflect.Method} or the {@link java.lang.reflect.Method} itself in case of a {@link
     * java.lang.reflect.Proxy} stubbing for example.
     *
     * @return the annotated element
     */
    AnnotatedElement getAnnotatedElement();

}
