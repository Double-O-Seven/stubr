package ch.leadrian.stubr.core;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Exception indicating misuse of a {@link Stubber} or {@link StubbingStrategy}.
 */
public class StubbingException extends IllegalStateException {

    static final long serialVersionUID = 4249103561555675566L;

    private final transient StubbingSite site;
    private final transient Type type;

    /**
     * Constructor that derives the message from the given {@link StubbingSite} and the given {@link Type} and uses
     * {@code message} to provide detailed information about the error.
     *
     * @param message detailed message
     * @param site    the {@link StubbingSite} at which the exception has been thrown
     * @param type    the {@link Type} which caused a {@link Stubber} or {@link StubbingStrategy} to throw the
     *                exception
     */
    public StubbingException(String message, StubbingSite site, Type type) {
        super(String.format("Cannot stub %s at %s: %s", type, site, message));
        this.site = site;
        this.type = type;
    }

    /**
     * Constructor that derives the message from the given {@link StubbingSite} and the given {@link Type}.
     *
     * @param site the {@link StubbingSite} at which the exception has been thrown
     * @param type the {@link Type} which caused a {@link Stubber} or {@link StubbingStrategy} to throw the exception
     */
    public StubbingException(StubbingSite site, Type type) {
        super(String.format("Cannot stub %s at %s", type, site));
        this.site = site;
        this.type = type;
    }

    /**
     * Generic constructor used for exceptions when no contextual information about the stubbing process is available.
     *
     * @param message error message indicating what went wrong
     */
    public StubbingException(String message) {
        super(message);
        this.site = null;
        this.type = null;
    }

    /**
     * @return the {@link StubbingSite} at which the exception has been thrown
     */
    public Optional<StubbingSite> getSite() {
        return Optional.ofNullable(site);
    }

    /**
     * @return the {@link Type} which caused a {@link Stubber} or {@link StubbingStrategy} to throw the exception
     */
    public Optional<Type> getType() {
        return Optional.ofNullable(type);
    }

}
