package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.RootStubber;
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
    private RootStubber rootStubber;

    @Override
    public void beforeEach(ExtensionContext context) {
        rootStubber = rootStubberFactory.create(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        rootStubber = null;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(Stub.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Type parameterizedType = parameterContext.getParameter().getParameterizedType();
        ParameterResolverStubbingSite site = new ParameterResolverStubbingSite(parameterContext, extensionContext);
        return rootStubber.stub(parameterizedType, site);
    }

}
