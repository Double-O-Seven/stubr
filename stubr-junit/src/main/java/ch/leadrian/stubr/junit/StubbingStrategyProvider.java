package ch.leadrian.stubr.junit;

import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;

public interface StubbingStrategyProvider {

    List<? extends StubbingStrategy> getStubbingStrategies(ExtensionContext extensionContext);

}
