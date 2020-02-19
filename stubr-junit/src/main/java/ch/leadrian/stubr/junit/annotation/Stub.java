package ch.leadrian.stubr.junit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation signaling that a parameter for a test method or fixture should be stubbed with {@link
 * ch.leadrian.stubr.junit.Stubr}.
 * <p>
 * If this annotation is present on another annotation type, using {@code @Stub} directly is not necessary.
 *
 * @see ch.leadrian.stubr.junit.Stubr
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface Stub {
}
