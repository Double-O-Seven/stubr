package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.type.TypeLiteral;
import ch.leadrian.stubr.core.type.Types;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class RootStubber {

    public static RootStubberBuilder builder() {
        return new RootStubberImpl.Builder();
    }

    public static RootStubber compose(List<? extends RootStubber> rootStubbers) {
        return new CompositeRootStubber(rootStubbers);
    }

    public static RootStubber compose(RootStubber... rootStubbers) {
        return compose(asList(rootStubbers));
    }

    protected abstract Result<?> tryToStub(Type type, StubbingContext context);

    public final Result<?> tryToStub(Type type, StubbingSite site) {
        StubbingContext context = new StubbingContext(this, site);
        return tryToStub(type, context);
    }

    public final Result<?> tryToStub(Type type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    public final Object stub(Type type, StubbingSite site) {
        Result<?> result = tryToStub(type, site);
        if (result.isFailure()) {
            throw new IllegalStateException(String.format("Failed to stub instance of %s", type));
        }
        return result.getValue();
    }

    public final Object stub(Type type) {
        return stub(type, StubbingSites.unknown());
    }

    public final <T> Result<T> tryToStub(Class<T> type, StubbingSite site) {
        return tryToStub((Type) type, site).map(type::cast);
    }

    public final <T> Result<T> tryToStub(Class<T> type) {
        return tryToStub(type, StubbingSites.unknown());
    }

    public final <T> T stub(Class<T> type, StubbingSite site) {
        return type.cast(stub((Type) type, site));
    }

    public final <T> T stub(Class<T> type) {
        return stub(type, StubbingSites.unknown());
    }

    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getRawType(typeLiteral);
        return tryToStub(typeLiteral.getType(), site).map(rawType::cast);
    }

    public final <T> Result<T> tryToStub(TypeLiteral<T> typeLiteral) {
        return tryToStub(typeLiteral, StubbingSites.unknown());
    }

    public final <T> T stub(TypeLiteral<T> typeLiteral, StubbingSite site) {
        Class<T> rawType = getRawType(typeLiteral);
        return rawType.cast(stub(typeLiteral.getType(), site));
    }

    public final <T> T stub(TypeLiteral<T> typeLiteral) {
        return stub(typeLiteral, StubbingSites.unknown());
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getRawType(TypeLiteral<T> typeLiteral) {
        return (Class<T>) Types.getRawType(typeLiteral.getType()).orElseThrow(UnsupportedOperationException::new);
    }

}
