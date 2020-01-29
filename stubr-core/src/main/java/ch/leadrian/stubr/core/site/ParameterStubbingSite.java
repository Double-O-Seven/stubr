package ch.leadrian.stubr.core.site;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;

public interface ParameterStubbingSite extends AnnotatedStubbingSite {

    Parameter getParameter();

    @Override
    default AnnotatedElement getAnnotatedElement() {
        return getParameter();
    }

}
