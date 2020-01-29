package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

/**
 * A builder interface for {@link RootStubber}.
 * <p>
 * A {@link RootStubber} can be built by including other {@link RootStubber}s as base and/or adding various {@link
 * Stubber} to stub specific types.
 * <p>
 * An included {@link RootStubber} will be overridden by any other {@link RootStubber} that is added later, assuming the
 * latter {@link RootStubber} provides a conflicting stubbing strategy to stub a certain type. Any {@link RootStubber}
 * will be overridden by any configured {@link Stubber}.
 * <p>
 * An added {@link Stubber} will be overridden by any other {@link Stubber} that is added later, assuming the latter
 * {@link Stubber} accepts a types that is also accepted by the former {@link Stubber}.
 * <p>
 * {@link Stubber}s may be conditionally applied using a {@link Matcher} that matches against the type to be stubbed.
 * Conditionally applying a {@link Stubber} will result in the same behaviour as applying one that has been created
 * using {@link Stubber#when(Matcher)}.
 *
 * @see Stubber
 * @see RootStubber
 */
public interface RootStubberBuilder {

    /**
     * Adds an existing {@link RootStubber} as baseline for the {@link RootStubber} that is to be built. An included
     * {@link RootStubber} will be overridden by any other {@link RootStubber} that is added later, assuming the latter
     * {@link RootStubber} provides a conflicting stubbing strategy to stub a certain type. Any {@link RootStubber} will
     * be overridden by any configured {@link Stubber}.
     *
     * @param rootStubber a baseline {@link RootStubber}
     * @return this
     */
    RootStubberBuilder include(RootStubber rootStubber);

    /**
     * Adds a {@link Stubber}. An added {@link Stubber} will be overridden by any other {@link Stubber} that is added
     * later, assuming the latter {@link Stubber} accepts a types that is also accepted by the former {@link Stubber}.
     *
     * @param stubber {@link Stubber} used to stub instances of matching types
     * @return {@code this}
     */
    RootStubberBuilder stubWith(Stubber stubber);

    /**
     * Adds a {@link Stubber} that will only be applied, if the given {@link Matcher} matches the type to be stubbed.
     *
     * @param stubber {@link Stubber} used to stub instances of matching types
     * @param matcher {@link Matcher} used as a prerequisite for applying the given {@link Stubber}
     * @return {@code this}
     */
    RootStubberBuilder stubWith(Stubber stubber, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link Stubber}s in iteration order according to the behaviour defined for {@link
     * RootStubberBuilder#stubWith(Stubber)}.
     *
     * @param stubbers {@link Stubber}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(Stubber)
     */
    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers);

    /**
     * Adds multiple conditional {@link Stubber}s in iteration order according to the behaviour defined for {@link
     * RootStubberBuilder#stubWith(Stubber)} and {@link RootStubberBuilder#stubWith(Stubber, Matcher)}.
     *
     * @param stubbers {@link Stubber}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(Iterable)
     * @see RootStubberBuilder#stubWith(Stubber, Matcher)
     */
    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link Stubber}s in iteration order according to the behaviour defined for {@link
     * RootStubberBuilder#stubWith(Stubber)}.
     *
     * @param stubbers {@link Stubber}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(Stubber)
     */
    RootStubberBuilder stubWith(Stubber... stubbers);

    /**
     * Builds a {@link RootStubber} instance that will attempt to stub an instance for a certain type using the
     * configured {@link RootStubber} baselines and {@link Stubber}s.
     *
     * @return a concrete {@link RootStubber} instance
     */
    RootStubber build();

}
