package ch.leadrian.stubr.junit.annotation;

import ch.leadrian.stubr.junit.StubbingStrategyProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface StubWith {

    Class<? extends StubbingStrategyProvider>[] value();

}
