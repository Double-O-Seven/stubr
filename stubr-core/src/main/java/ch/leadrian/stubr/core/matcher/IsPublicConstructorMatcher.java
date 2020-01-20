package ch.leadrian.stubr.core.matcher;

import ch.leadrian.stubr.core.ConstructorMatcher;

import java.lang.reflect.Constructor;

import static java.lang.reflect.Modifier.isPublic;

enum IsPublicConstructorMatcher implements ConstructorMatcher {
    INSTANCE;

    @Override
    public boolean matches(Constructor<?> constructor) {
        return isPublic(constructor.getModifiers());
    }
}
