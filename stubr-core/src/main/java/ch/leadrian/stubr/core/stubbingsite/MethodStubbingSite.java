package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;

public interface MethodStubbingSite extends StubbingSite {

    Method getMethod();

}
