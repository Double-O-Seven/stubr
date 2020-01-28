package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.Optional;

public class StubbingException extends IllegalStateException {

    static final long serialVersionUID = 4249103561555675566L;

    private final transient StubbingSite site;
    private final transient Type type;

    public StubbingException(String message, StubbingSite site, Type type) {
        super(String.format("Cannot stub %s at %s: %s", type, site, message));
        this.site = site;
        this.type = type;
    }

    public StubbingException(StubbingSite site, Type type) {
        super(String.format("Cannot stub %s at %s", type, site));
        this.site = site;
        this.type = type;
    }

    public StubbingException(String message) {
        super(message);
        this.site = null;
        this.type = null;
    }

    public Optional<StubbingSite> getSite() {
        return Optional.ofNullable(site);
    }

    public Optional<Type> getType() {
        return Optional.ofNullable(type);
    }

}
