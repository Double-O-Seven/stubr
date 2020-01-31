package ch.leadrian.stubr.core.site;

import java.lang.reflect.Method;

public interface MethodStubbingSite extends ExecutableStubbingSite {

    Method getMethod();

    /**
     * {@inheritDoc}
     */
    @Override
    default Method getExecutable() {
        return getMethod();
    }

}
