package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Optional;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;

final class OptionalStubber implements Stubber {

    static final OptionalStubber INSTANCE = new OptionalStubber();

    private OptionalStubber() {
    }

    @Override
    public boolean accepts(Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return Optional.class == clazz;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return Optional.class == parameterizedType.getRawType();
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
    public Optional<?> stub(RootStubber rootStubber, Type type) {
        return getValueClass(type)
                .map(rootStubber::tryToStub)
                .filter(Result::isSuccess)
                .map(Result::getValue);
    }

    private Optional<Class<?>> getValueClass(Type type) {
        return accept(type, new TypeVisitor<Optional<Class<?>>>() {

            @Override
            public Optional<Class<?>> visit(Class<?> clazz) {
                return Optional.empty();
            }

            @Override
            public Optional<Class<?>> visit(ParameterizedType parameterizedType) {
                return accept(parameterizedType.getActualTypeArguments()[0], this);
            }

            @Override
            public Optional<Class<?>> visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType).flatMap(upperBound -> accept(upperBound, this));
            }

            @Override
            public Optional<Class<?>> visit(TypeVariable<?> typeVariable) {
                return Optional.empty();
            }
        });
    }
}
