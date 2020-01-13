package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

final class MapStubber<T extends Map> extends SimpleStubber<T> {

    private final Class<T> mapClass;
    private final Function<Map<Object, Object>, ? extends T> mapFactory;
    private final ToIntFunction<? super StubbingContext> mapSize;

    MapStubber(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, ToIntFunction<? super StubbingContext> mapSize) {
        requireNonNull(mapClass, "mapClass");
        requireNonNull(mapFactory, "mapFactory");
        requireNonNull(mapSize, "mapSize");
        this.mapClass = mapClass;
        this.mapFactory = mapFactory;
        this.mapSize = mapSize;
    }

    @Override
    protected boolean accepts(StubbingContext context, Class<?> type) {
        return mapClass == type && mapSize.applyAsInt(context) == 0;
    }

    @Override
    protected boolean accepts(StubbingContext context, ParameterizedType type) {
        return mapClass == type.getRawType() && type.getActualTypeArguments().length == 2;
    }

    @Override
    protected T stub(StubbingContext context, Class<?> type) {
        return mapFactory.apply(emptyMap());
    }

    @Override
    protected T stub(StubbingContext context, ParameterizedType type) {
        Type keyType = type.getActualTypeArguments()[0];
        Type valueType = type.getActualTypeArguments()[1];
        StubbingSite keySite = StubbingSites.parameterizedType(context.getSite(), type, 0);
        StubbingSite valueSite = StubbingSites.parameterizedType(context.getSite(), type, 1);
        int size = mapSize.applyAsInt(context);
        Map<Object, Object> values = new HashMap<>(size);
        IntStream.iterate(0, i -> i + 1)
                .limit(size)
                .forEach(i -> {
                    Object key = context.getStubber().stub(keyType, keySite);
                    Object value = context.getStubber().stub(valueType, valueSite);
                    values.put(key, value);
                });
        return mapFactory.apply(values);
    }

}
