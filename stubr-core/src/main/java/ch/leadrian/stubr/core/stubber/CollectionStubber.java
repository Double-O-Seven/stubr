package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.StubbingSite;
import ch.leadrian.stubr.core.stubbingsite.StubbingSites;
import ch.leadrian.stubr.core.util.TypeVisitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

final class CollectionStubber<T extends Collection<Object>> implements Stubber {

    private final Class<T> collectionClass;
    private final Function<List<Object>, ? extends T> collectionFactory;
    private final IntSupplier collectionSize;

    CollectionStubber(Class<T> collectionClass, Function<List<Object>, ? extends T> collectionFactory, IntSupplier collectionSize) {
        this.collectionClass = collectionClass;
        this.collectionFactory = collectionFactory;
        this.collectionSize = collectionSize;
    }

    @Override
    public boolean accepts(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<Boolean>() {

            @Override
            public Boolean visit(Class<?> clazz) {
                return collectionClass == clazz && collectionSize.getAsInt() == 0;
            }

            @Override
            public Boolean visit(ParameterizedType parameterizedType) {
                return collectionClass == parameterizedType.getRawType() && parameterizedType.getActualTypeArguments().length == 1;
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
    public T stub(StubbingContext context, Type type) {
        return accept(type, new TypeVisitor<T>() {

            @Override
            public T visit(Class<?> clazz) {
                return collectionFactory.apply(emptyList());
            }

            @Override
            public T visit(ParameterizedType parameterizedType) {
                Type valueType = parameterizedType.getActualTypeArguments()[0];
                StubbingSite site = StubbingSites.parameterizedType(context.getSite(), parameterizedType, 0);
                List<Object> values = IntStream.iterate(0, i -> i + 1)
                        .limit(collectionSize.getAsInt())
                        .mapToObj(i -> context.getStubber().stub(valueType, site))
                        .collect(toList());
                return collectionFactory.apply(values);
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
