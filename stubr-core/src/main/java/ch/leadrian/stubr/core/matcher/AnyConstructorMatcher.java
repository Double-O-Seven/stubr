package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;

import java.lang.reflect.Constructor;

enum AnyConstructorMatcher implements ConstructorMatcher {
    INSTANCE;

    @Override
    public boolean matches(Constructor<?> constructor) {
        return true;
    }
}
