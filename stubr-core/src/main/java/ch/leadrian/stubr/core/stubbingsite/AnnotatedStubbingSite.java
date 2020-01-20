package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.AnnotatedElement;

public interface AnnotatedStubbingSite extends StubbingSite {

    AnnotatedElement getAnnotatedElement();

}
