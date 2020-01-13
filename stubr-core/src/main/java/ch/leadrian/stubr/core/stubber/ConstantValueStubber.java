package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getLowerBound;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;
import static java.util.Objects.requireNonNull;

final class ConstantValueStubber implements Stubber {

    private final Type valueType;
    private final Object value;

    ConstantValueStubber(Type valueClass, Object value) {
        requireNonNull(valueClass, "valueType");
        requireNonNull(value, "value");
        this.valueType = valueClass;
        this.value = value;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return valueType == clazz;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return valueType.equals(parameterizedType);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                if (valueType.equals(wildcardType)) {
                    return true;
                }

                if (getLowerBound(wildcardType).filter(type -> accept(type, this)).isPresent()) {
                    return true;
                } else {
                    return getOnlyUpperBound(wildcardType).filter(type -> accept(type, this)).isPresent();
                }
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return valueType.equals(typeVariable);
            }

            @Override
            public Boolean visit(GenericArrayType genericArrayType) {
                return valueType.equals(genericArrayType);
            }
        });
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return value;
    }
}
