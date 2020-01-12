package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;

final class ConstantValueStubber implements Stubber {

    private final Class<?> valueClass;
    private final Object value;

    ConstantValueStubber(Class<?> valueClass, Object value) {
        this.valueClass = valueClass;
        this.value = value;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return valueClass == clazz;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getRawType(), this);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType)
                        .filter(upperBound -> accept(upperBound, this))
                        .isPresent();
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return false;
            }
        });
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return value;
    }
}
