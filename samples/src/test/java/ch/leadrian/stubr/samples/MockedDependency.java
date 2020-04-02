package ch.leadrian.stubr.samples;

import ch.leadrian.stubr.junit.annotation.Stub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Stub
public @interface MockedDependency {
}
