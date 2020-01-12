package ch.leadrian.stubr.core.stubbingsite;

import ch.leadrian.stubr.core.StubbingSite;

import java.lang.reflect.Parameter;

public interface ParameterStubbingSite extends StubbingSite {

    Parameter getParameter();

}
