package ch.leadrian.stubr.core.site;

import java.lang.reflect.Parameter;

public interface ParameterStubbingSite extends AnnotatedStubbingSite {

    Parameter getParameter();

    @Override
    default Parameter getAnnotatedElement() {
        return getParameter();
    }

}
