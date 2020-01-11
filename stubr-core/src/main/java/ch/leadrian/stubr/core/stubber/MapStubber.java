package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;
import static java.util.Collections.emptyMap;

final class MapStubber<T extends Map<Object, Object>> implements Stubber {

    private final Class<T> mapClass;
    private final Function<Map<Object, Object>, ? extends T> mapFactory;
    private final IntSupplier mapSize;

    MapStubber(Class<T> mapClass, Function<Map<Object, Object>, ? extends T> mapFactory, IntSupplier mapSize) {
        this.mapClass = mapClass;
        this.mapFactory = mapFactory;
        this.mapSize = mapSize;
    }

    @Override
    public boolean accepts(Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return mapClass == clazz && mapSize.getAsInt() == 0;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return mapClass == parameterizedType.getRawType() && parameterizedType.getActualTypeArguments().length == 2;
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
    public Object stub(RootStubber rootStubber, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return mapFactory.apply(emptyMap());
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                Type keyType = parameterizedType.getActualTypeArguments()[0];
                Type valueType = parameterizedType.getActualTypeArguments()[1];
                Map<Object, Object> values = new HashMap<>(mapSize.getAsInt());
                IntStream.iterate(0, i -> i + 1)
                        .limit(mapSize.getAsInt())
                        .forEach(i -> {
                            Object key = rootStubber.stub(keyType);
                            Object value = rootStubber.stub(valueType);
                            values.put(key, value);
                        });
                return mapFactory.apply(values);
            }

            @Override
            public T visit(WildcardType wildcardType) {
                return getOnlyUpperBound(wildcardType)
                        .map(upperBound -> accept(upperBound, this))
                        .orElseThrow(IllegalStateException::new);
            }

            @Override
            public T visit(TypeVariable<?> typeVariable) {
                throw new IllegalStateException();
            }
        });
    }
}
