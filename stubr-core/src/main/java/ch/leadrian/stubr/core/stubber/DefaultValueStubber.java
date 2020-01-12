package ch.leadrian.stubr.core.stubber;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ch.leadrian.stubr.core.util.Types.getActualClass;

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
    public boolean accepts(StubbingContext context, Type type) {
        return defaultValuesMap.get(type).isPresent();
    }

    @Override
    public Object stub(StubbingContext context, Type type) {
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
            return getActualClass(type).map(defaultValuesByClass::get);
        }

    }
}
