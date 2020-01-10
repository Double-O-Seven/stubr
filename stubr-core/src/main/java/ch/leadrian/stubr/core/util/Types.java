package ch.leadrian.stubr.core.util;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Optional;

public final class Types {

    private Types() {
    }

    public static Optional<Type> getLowerBound(WildcardType type) {
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 1) {
            return Optional.of(lowerBounds[0]);
        }
        return Optional.empty();
    }
}
