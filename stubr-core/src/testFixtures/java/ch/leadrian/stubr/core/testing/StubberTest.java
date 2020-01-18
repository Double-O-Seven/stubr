package ch.leadrian.stubr.core.testing;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import org.junit.jupiter.api.DynamicTest;

interface StubberTest {

    DynamicTest toDynamicTest(Stubber stubber, StubbingContext context);

}
