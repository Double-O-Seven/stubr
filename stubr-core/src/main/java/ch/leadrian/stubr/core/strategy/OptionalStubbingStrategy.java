package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.Result;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.site.StubbingSites;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

abstract class OptionalStubbingStrategy extends SimpleStubbingStrategy<Optional<Object>> {

    static final OptionalStubbingStrategy EMPTY = new EmptyStubbingStrategy();
    static final OptionalStubbingStrategy PRESENT = new PresentStubbingStrategy();
    static final OptionalStubbingStrategy PRESENT_IF_POSSIBLE = new PresentIfPossibleStubbingStrategy();

    private OptionalStubbingStrategy() {
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return Optional.class == type && isEmptyAllowed();
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
        return stubOptional(context, type);
    }

    protected abstract Optional<Object> stubOptional(StubbingContext context, ParameterizedType type);

    protected abstract boolean isEmptyAllowed();

    private static final class EmptyStubbingStrategy extends OptionalStubbingStrategy {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, ParameterizedType type) {
            return Optional.empty();
        }

        @Override
        protected boolean isEmptyAllowed() {
            return true;
        }

    }

    private static abstract class AbstractPresentStubbingStrategy extends OptionalStubbingStrategy {

        @Override
        protected final Optional<Object> stubOptional(StubbingContext context, ParameterizedType type) {
            StubbingSite site = StubbingSites.parameterizedType(context.getSite(), type, 0);
            Type valueType = type.getActualTypeArguments()[0];
            return stubOptional(context, site, valueType);
        }

        protected abstract Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType);

    }

    private static final class PresentStubbingStrategy extends AbstractPresentStubbingStrategy {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType) {
            return Optional.ofNullable(context.getStubber().stub(valueType, site));
        }

        @Override
        protected boolean isEmptyAllowed() {
            return false;
        }

    }

    private static final class PresentIfPossibleStubbingStrategy extends AbstractPresentStubbingStrategy {

        @Override
        protected Optional<Object> stubOptional(StubbingContext context, StubbingSite site, Type valueType) {
            Result<?> result = context.getStubber().tryToStub(valueType, site);
            if (result.isSuccess()) {
                return Optional.ofNullable(result.getValue());
            }
            return Optional.empty();
        }

        @Override
        protected boolean isEmptyAllowed() {
            return true;
        }

    }

}
