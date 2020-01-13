package ch.leadrian.stubr.core.stubber;

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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getRawType;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

final class MapStubber<T extends Map> implements Stubber {

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
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return mapClass == clazz && mapSize.applyAsInt(context) == 0;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return mapClass == parameterizedType.getRawType() && parameterizedType.getActualTypeArguments().length == 2;
            }

            @Override
            public Boolean visit(WildcardType wildcardType) {
                return getRawType(wildcardType)
                        .filter(type -> accept(type, this))
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

    @Override
    public Object stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return mapFactory.apply(emptyMap());
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                Type keyType = parameterizedType.getActualTypeArguments()[0];
                Type valueType = parameterizedType.getActualTypeArguments()[1];
                StubbingSite keySite = StubbingSites.parameterizedType(context.getSite(), parameterizedType, 0);
                StubbingSite valueSite = StubbingSites.parameterizedType(context.getSite(), parameterizedType, 1);
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

            @Override
            public T visit(WildcardType wildcardType) {
                return getRawType(wildcardType)
                        .map(type -> accept(type, this))
                        .orElseThrow(IllegalStateException::new);
            }

            @Override
            public T visit(TypeVariable<?> typeVariable) {
                throw new IllegalStateException();
            }

            @Override
            public T visit(GenericArrayType genericArrayType) {
                throw new IllegalStateException();
            }
        });
    }
}
