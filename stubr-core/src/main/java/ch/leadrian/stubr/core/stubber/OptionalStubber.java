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
import static ch.leadrian.stubr.core.util.Types.getMostSpecificType;

enum OptionalStubber implements Stubber {
    EMPTY(EmptyStubbingStrategy.INSTANCE),
    PRESENT(new PresentStubbingStrategy()),
    PRESENT_IF_POSSIBLE(new PresentIfPossibleStubbingStrategy());

    private final StubbingStrategy strategy;

    OptionalStubber(StubbingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return Optional.class == clazz && strategy.isEmptyAllowed();
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return Optional.class == parameterizedType.getRawType();
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return accept(wildcardType, this);
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

    @Override
    public Optional<Object> stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Optional<Object>>() {

            @Override
            public Optional<Object> visit(Class<?> clazz) {
                return Optional.empty();
            }

            @Override
            public Optional<Object> visit(ParameterizedType parameterizedType) {
                return strategy.stub(context, parameterizedType);
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

    private interface StubbingStrategy {

        Optional<Object> stub(StubbingContext context, ParameterizedType type);

        boolean isEmptyAllowed();

    }

    private enum EmptyStubbingStrategy implements StubbingStrategy {
        INSTANCE;

        @Override
        public Optional<Object> stub(StubbingContext context, ParameterizedType type) {
            return Optional.empty();
        }

        @Override
        public boolean isEmptyAllowed() {
            return true;
        }
    }

    private static abstract class AbstractStubbingStrategy implements StubbingStrategy {

        @Override
        public final Optional<Object> stub(StubbingContext context, ParameterizedType type) {
            StubbingSite site = StubbingSites.parameterizedType(context.getSite(), type, 0);
            Type valueType = type.getActualTypeArguments()[0];
            return stub(context, site, valueType);
        }

        protected abstract Optional<Object> stub(StubbingContext context, StubbingSite site, Type valueType);
    }

    private static final class PresentStubbingStrategy extends AbstractStubbingStrategy {

        @Override
        protected Optional<Object> stub(StubbingContext context, StubbingSite site, Type valueType) {
            return Optional.ofNullable(context.getStubber().stub(valueType, site));
        }

        @Override
        public boolean isEmptyAllowed() {
            return false;
        }
    }

    private static final class PresentIfPossibleStubbingStrategy extends AbstractStubbingStrategy {

        @Override
        protected Optional<Object> stub(StubbingContext context, StubbingSite site, Type valueType) {
            Result<?> result = context.getStubber().tryToStub(valueType, site);
            if (result.isSuccess()) {
                return Optional.ofNullable(result.getValue());
            }
            return Optional.empty();
        }

        @Override
        public boolean isEmptyAllowed() {
            return true;
        }
    }
}
