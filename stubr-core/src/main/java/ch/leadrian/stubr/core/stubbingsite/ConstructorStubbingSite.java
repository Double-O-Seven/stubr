package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Constructor;

public interface ConstructorStubbingSite extends StubbingSite {

    Constructor<?> getConstructor();

}
