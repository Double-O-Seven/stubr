package ch.leadrian.stubr.junit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation signaling that a parameter for a test method or fixture should be stubbed with {@link
 * ch.leadrian.stubr.junit.Stubr}.
 *
 * @see ch.leadrian.stubr.junit.Stubr
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Stub {
}
