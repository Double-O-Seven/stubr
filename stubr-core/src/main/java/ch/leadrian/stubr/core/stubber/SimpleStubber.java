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
import static ch.leadrian.stubr.core.util.Types.getExplicitBound;

public abstract class SimpleStubber<T> implements Stubber {

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return accepts(context, clazz);
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return accepts(context, parameterizedType);
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getExplicitBound(wildcardType)
                        .filter(t -> accept(t, this))
                        .isPresent();
            }

            @Override
            public Boolean visit(TypeVariable<?> typeVariable) {
                return false;
            }

            @Override
            public Boolean visit(GenericArrayType genericArrayType) {
                return false;
            }
        });
    }

    protected abstract boolean accepts(StubbingContext context, Class<?> type);

    protected abstract boolean accepts(StubbingContext context, ParameterizedType type);

    @Override
    public T stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return stub(context, clazz);
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                return stub(context, parameterizedType);
            }

            @Override
            public T visit(WildcardType wildcardType) {
                return getExplicitBound(wildcardType)
                        .map(t -> accept(t, this))
                        .orElseThrow(UnsupportedOperationException::new);
            }

            @Override
            public T visit(TypeVariable<?> typeVariable) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T visit(GenericArrayType genericArrayType) {
                throw new UnsupportedOperationException();
            }
        });
    }

    protected abstract T stub(StubbingContext context, Class<?> type);

    protected abstract T stub(StubbingContext context, ParameterizedType type);
}
