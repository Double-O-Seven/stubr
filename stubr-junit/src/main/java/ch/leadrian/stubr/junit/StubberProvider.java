package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

@FunctionalInterface
public interface StubberProvider {

    List<? extends StubbingStrategy> getStubbers(ExtensionContext extensionContext);

}
