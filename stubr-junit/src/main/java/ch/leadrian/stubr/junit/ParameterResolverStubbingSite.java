package ch.leadrian.stubr.junit;

import ch.leadrian.equalizer.EqualsAndHashCode;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.ParameterStubbingSite;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;

public final class ParameterResolverStubbingSite implements ParameterStubbingSite {

    private static final EqualsAndHashCode<ParameterResolverStubbingSite> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(ParameterResolverStubbingSite.class)
            .compareAndHash(ParameterResolverStubbingSite::getParameterContext)
            .compareAndHash(ParameterResolverStubbingSite::getExtensionContext)
            .build();

    private final ParameterContext parameterContext;
    private final ExtensionContext extensionContext;

    ParameterResolverStubbingSite(ParameterContext parameterContext, ExtensionContext extensionContext) {
        this.parameterContext = parameterContext;
        this.extensionContext = extensionContext;
    }

    public ParameterContext getParameterContext() {
        return parameterContext;
    }

    public ExtensionContext getExtensionContext() {
        return extensionContext;
    }

    @Override
    public Parameter getParameter() {
        return parameterContext.getParameter();
    }

    @Override
    public Optional<StubbingSite> getParent() {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }
}
