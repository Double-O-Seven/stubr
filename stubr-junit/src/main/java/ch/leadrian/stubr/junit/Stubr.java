package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.junit.annotation.Stub;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Type;

/**
 * A JUnit extension that provides stubs to a test case.
 * <p>
 * A method parameter of a test case or a fixture method (annotated with {@link org.junit.jupiter.api.BeforeEach} for
 * example) will be provided by {@code Stubr} if the parameter has been annotated with {@link Stub}.
 * <p>
 * For each test case, a {@link Stubber} is configured using the configuration provided through the annotations {@link
 * ch.leadrian.stubr.junit.annotation.Include}, {@link ch.leadrian.stubr.junit.annotation.StubberBaseline} and {@link
 * ch.leadrian.stubr.junit.annotation.StubWith}. The annotations may be present on the test method, the test class, or
 * any parent class of the test class, given the test class is a nested class. Configurations on the test method will be
 * merged with configurations on the test class and in case of a conflict, a configuration on a test method will
 * override the conflicting configuration on the test class. The same behaviour applies for nested test classes and
 * their parent test classes, where configurations of a parent test class may be overridden.
 * <p>
 * Beware that only non-static test fixtures are supported, methods annotated with {@link
 * org.junit.jupiter.api.BeforeEach} for example. Static test fixtures annotated with {@link
 * org.junit.jupiter.api.BeforeAll} are not supported.
 *
 * @see Stub
 * @see ch.leadrian.stubr.junit.annotation.Include
 * @see ch.leadrian.stubr.junit.annotation.StubberBaseline
 * @see ch.leadrian.stubr.junit.annotation.StubWith
 */
public final class Stubr implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final StubberFactory stubberFactory = new StubberFactory();
    private Stubber stubber;

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        stubber = stubberFactory.create(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        stubber = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return stubber != null && parameterContext.getParameter().isAnnotationPresent(Stub.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Type parameterizedType = parameterContext.getParameter().getParameterizedType();
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);
        return stubber.stub(parameterizedType, site);
    }

}
