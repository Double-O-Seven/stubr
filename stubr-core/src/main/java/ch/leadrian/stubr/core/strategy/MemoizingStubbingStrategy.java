package ch.leadrian.stubr.core.strategy;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingStrategy;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

final class MemoizingStubbingStrategy implements StubbingStrategy {

    private final Map<Type, Object> memoizedStubsByType = new ConcurrentHashMap<>();
    private final StubbingStrategy delegate;

    MemoizingStubbingStrategy(StubbingStrategy delegate) {
        requireNonNull(delegate, "delegate");
        this.delegate = delegate;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return delegate.accepts(context, type);
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
        return memoizedStubsByType.computeIfAbsent(type, t -> delegate.stub(context, t));
    }

}
