package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

final class CollectionStubber<T extends Collection> extends SimpleStubber<T> {

    private final Class<T> collectionClass;
    private final Function<List<Object>, ? extends T> collectionFactory;
    private final ToIntFunction<? super StubbingContext> collectionSize;

    CollectionStubber(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, ToIntFunction<? super StubbingContext> collectionSize) {
        requireNonNull(collectionClass, "collectionClass");
        requireNonNull(collectionFactory, "collectionFactory");
        requireNonNull(collectionSize, "collectionSize");
        this.collectionClass = collectionClass;
        this.collectionFactory = collectionFactory;
        this.collectionSize = collectionSize;
    }

    @Override
    protected boolean acceptsClass(StubbingContext context, Class<?> type) {
        return collectionClass == type && collectionSize.applyAsInt(context) == 0;
    }

    @Override
    protected boolean acceptsParameterizedType(StubbingContext context, ParameterizedType type) {
        return collectionClass == type.getRawType() && type.getActualTypeArguments().length == 1;
    }

    @Override
    protected T stubClass(StubbingContext context, Class<?> type) {
        return collectionFactory.apply(emptyList());
    }

    @Override
    protected T stubParameterizedType(StubbingContext context, ParameterizedType type) {
        Type valueType = type.getActualTypeArguments()[0];
        StubbingSite site = StubbingSites.parameterizedType(context.getSite(), type, 0);
        List<Object> values = IntStream.iterate(0, i -> i + 1)
                .limit(collectionSize.applyAsInt(context))
                .mapToObj(i -> context.getStubber().stub(valueType, site))
                .collect(toList());
        return collectionFactory.apply(values);
    }

}
