package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingException;
import ch.leadrian.stubr.core.type.TypeVisitor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static ch.leadrian.stubr.core.type.TypeVisitor.accept;
import static ch.leadrian.stubr.core.type.Types.getExplicitBound;

public abstract class SimpleStubber<T> implements Stubber {

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return acceptsClass(context, clazz);
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return acceptsParameterizedType(context, parameterizedType);
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

    protected abstract boolean acceptsClass(StubbingContext context, Class<?> type);

    protected abstract boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type);

    @Override
    public T stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return stubClass(context, clazz);
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                return stubParameterizedType(context, parameterizedType);
            }

            @Override
            public T visit(WildcardType wildcardType) {
                return getExplicitBound(wildcardType)
                        .map(t -> accept(t, this))
                        .orElseThrow(() -> new StubbingException(context.getSite(), wildcardType));
            }

            @Override
            public T visit(TypeVariable<?> typeVariable) {
                throw new StubbingException(context.getSite(), typeVariable);
            }

            @Override
            public T visit(GenericArrayType genericArrayType) {
                throw new StubbingException(context.getSite(), genericArrayType);
            }
        });
    }

    protected abstract T stubClass(StubbingContext context, Class<?> type);

    protected abstract T stubParameterizedType(StubbingContext context, ParameterizedType type);
}
