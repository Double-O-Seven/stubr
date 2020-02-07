package ch.leadrian.stubr.junit.annotation;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubberBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ch.leadrian.stubr.core.Stubbers.defaultStubber;
import static ch.leadrian.stubr.core.Stubbers.minimalStubber;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface StubberBaseline {

    Variant value() default Variant.DEFAULT;

    enum Variant {
        DEFAULT {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder().include(defaultStubber());
            }

        },
        MINIMAL {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder().include(minimalStubber());
            }
        },
        EMPTY {
            @Override
            public StubberBuilder getBuilder() {
                return Stubber.builder();
            }
        };

        public abstract StubberBuilder getBuilder();
    }

}
