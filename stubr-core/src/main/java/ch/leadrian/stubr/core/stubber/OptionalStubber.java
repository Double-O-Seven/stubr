package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

final class OptionalStubber extends SimpleStubber<Optional<Object>> {

    static final OptionalStubber EMPTY = new OptionalStubber(EmptyStubbingStrategy.INSTANCE);
    static final OptionalStubber PRESENT = new OptionalStubber(new PresentStubbingStrategy());
    static final OptionalStubber PRESENT_IF_POSSIBLE = new OptionalStubber(new PresentIfPossibleStubbingStrategy());

    private final StubbingStrategy strategy;

    private OptionalStubber(StubbingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return Optional.class == type && strategy.isEmptyAllowed();
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType parameterizedType) {
        return Optional.class == parameterizedType.getRawType();
    }

    @Override
    protected Optional<Object> stubClass(StubbingContext context, Class<?> type) {
        return Optional.empty();
    }

    @Override
    protected Optional<Object> stubParameterizedType(StubbingContext context, ParameterizedType type) {
        return strategy.stub(context, type);
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
