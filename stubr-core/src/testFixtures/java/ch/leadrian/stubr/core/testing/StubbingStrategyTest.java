package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;
import org.junit.jupiter.api.DynamicTest;

interface StubbingStrategyTest {

    DynamicTest toDynamicTest(StubbingStrategy stubbingStrategy, StubbingContext context);

}