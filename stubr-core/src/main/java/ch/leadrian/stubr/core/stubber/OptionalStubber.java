package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getActualClass;
import static ch.leadrian.stubr.core.util.Types.getMostSpecificType;

enum OptionalStubber implements Stubber {
    INSTANCE;

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return getActualClass(type)
                .filter(Optional.class::equals)
                .isPresent();
    }

    @Override
    public Optional<Object> stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Optional<Object>>() {

            @Override
            public Optional<Object> visit(Class<?> clazz) {
                return Optional.empty();
            }

            @Override
            public Optional<Object> visit(ParameterizedType parameterizedType) {
                StubbingSite site = StubbingSites.parameterizedType(context.getSite(), parameterizedType, 0);
                Result<?> result = context.getStubber().tryToStub(parameterizedType.getActualTypeArguments()[0], site);
                if (result.isSuccess()) {
                    return Optional.of(result);
                }
                return Optional.empty();
            }

            @Override
            public Optional<Object> visit(WildcardType wildcardType) {
                return getMostSpecificType(wildcardType).flatMap(upperBound -> accept(upperBound, this));
            }

            @Override
            public Optional<Object> visit(TypeVariable<?> typeVariable) {
                return Optional.empty();
            }

            @Override
            public Optional<Object> visit(GenericArrayType genericArrayType) {
                return Optional.empty();
            }
        });
    }
}
