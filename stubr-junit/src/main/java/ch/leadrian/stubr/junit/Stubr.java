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

public final class Stubr implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private final RootStubberFactory rootStubberFactory = new RootStubberFactory();
    private Stubber stubber;

    @Override
    public void beforeEach(ExtensionContext context) {
        stubber = rootStubberFactory.create(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        stubber = null;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(Stub.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Type parameterizedType = parameterContext.getParameter().getParameterizedType();
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);
        return stubber.stub(parameterizedType, site);
    }

}
