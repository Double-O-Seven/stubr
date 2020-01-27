package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.RootStubber;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

@FunctionalInterface
public interface RootStubberProvider {

    List<? extends RootStubber> getRootStubbers(ExtensionContext extensionContext);

}
