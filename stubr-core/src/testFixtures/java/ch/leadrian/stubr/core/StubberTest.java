package ch.leadrian.stubr.core;

import org.junit.jupiter.api.DynamicTest;

interface StubberTest {

    DynamicTest toDynamicTest(Stubber stubber, StubbingContext context);

}
