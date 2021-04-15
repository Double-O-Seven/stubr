/*
 * Copyright (C) 2021 Adrian-Philipp Leuenberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * Wraps another exception.
     *
     * @param cause the original cause of the exception
     */
    public StubbingException(Throwable cause) {
        super(cause);
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
