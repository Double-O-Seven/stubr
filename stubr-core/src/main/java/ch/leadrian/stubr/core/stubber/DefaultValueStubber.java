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
import java.util.Optional;

import static ch.leadrian.stubr.core.util.TypeVisitor.accept;
import static ch.leadrian.stubr.core.util.Types.getOnlyUpperBound;

final class DefaultValueStubber implements Stubber {

    static final DefaultValueStubber INSTANCE = new DefaultValueStubber();

    private final DefaultValuesMap defaultValuesMap = new DefaultValuesMap()
            .add(boolean.class, false)
            .add(false)
            .add(byte.class, (byte) 0)
            .add((byte) 0)
            .add(short.class, (short) 0)
            .add((short) 0)
            .add(char.class, '?')
            .add('?')
            .add(int.class, 0)
            .add(0)
            .add(long.class, 0L)
            .add(0L)
            .add(float.class, 0f)
            .add(0f)
            .add(double.class, 0.0)
            .add(0.0)
            .add("");

    private DefaultValueStubber() {
    }

    @Override
    public boolean accepts(Type type) {
        return defaultValuesMap.get(type).isPresent();
    }

    @Override
    public Object stub(RootStubber rootStubber, Type type) {
        return defaultValuesMap.get(type).orElseThrow(IllegalStateException::new);
    }

    private static final class DefaultValuesMap {

        private final Map<Class<?>, Object> defaultValuesByClass = new HashMap<>();

        DefaultValuesMap add(Object value) {
            defaultValuesByClass.put(value.getClass(), value);
            return this;
        }

        <T> DefaultValuesMap add(Class<T> clazz, T value) {
            defaultValuesByClass.put(clazz, value);
            return this;
        }

        Optional<Object> get(Type type) {
            return accept(type, new TypeVisitor<Optional<Object>>() {

                @Override
                public Optional<Object> visit(Class<?> clazz) {
                    return Optional.ofNullable(defaultValuesByClass.get(clazz));
                }

                @Override
                public Optional<Object> visit(ParameterizedType parameterizedType) {
                    return accept(parameterizedType.getRawType(), this);
                }

                @Override
                public Optional<Object> visit(WildcardType wildcardType) {
                    return getOnlyUpperBound(wildcardType).flatMap(upperBound -> accept(upperBound, this));
                }

                @Override
                public Optional<Object> visit(TypeVariable<?> typeVariable) {
                    return Optional.empty();
                }
            });
        }

    }
}
