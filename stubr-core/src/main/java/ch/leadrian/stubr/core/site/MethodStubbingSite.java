package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Method;

public interface MethodStubbingSite extends StubbingSite {

    Method getMethod();

}
