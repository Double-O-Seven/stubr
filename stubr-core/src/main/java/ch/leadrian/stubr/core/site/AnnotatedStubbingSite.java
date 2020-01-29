package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.AnnotatedElement;

public interface AnnotatedStubbingSite extends StubbingSite {

    AnnotatedElement getAnnotatedElement();

}
