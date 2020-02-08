package ch.leadrian.stubr.junit.annotation;

import ch.leadrian.stubr.junit.StubberProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation signaling that the given {@link StubberProvider} should be used to configure a {@link
 * ch.leadrian.stubr.core.Stubber} for a test case.
 * <p>
 * For the annotation to work, the test must be extended with {@link ch.leadrian.stubr.junit.Stubr}.
 *
 * @see StubberProvider
 * @see ch.leadrian.stubr.junit.Stubr
 * @see org.junit.jupiter.api.extension.ExtendWith
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Include {

    /**
     * The class of the {@link StubberProvider}. The {@link ch.leadrian.stubr.core.Stubber}s provided by the provider
     * will be included in the {@link ch.leadrian.stubr.core.Stubber} configured for a test case.
     *
     * @return class of {@link StubberProvider}
     * @see StubberProvider
     */
    Class<? extends StubberProvider>[] value();

}
