package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

/**
 * A builder interface for {@link RootStubber}.
 * <p>
 * A {@link RootStubber} can be built by including other {@link RootStubber}s as base and/or adding various {@link
 * StubbingStrategy} to stub specific types.
 * <p>
 * An included {@link RootStubber} will be overridden by any other {@link RootStubber} that is added later, assuming the
 * latter {@link RootStubber} provides a conflicting stubbing strategy to stub a certain type. Any {@link RootStubber}
 * will be overridden by any configured {@link StubbingStrategy}.
 * <p>
 * An added {@link StubbingStrategy} will be overridden by any other {@link StubbingStrategy} that is added later,
 * assuming the latter {@link StubbingStrategy} accepts a types that is also accepted by the former {@link
 * StubbingStrategy}.
 * <p>
 * {@link StubbingStrategy}s may be conditionally applied using a {@link Matcher} that matches against the type to be
 * stubbed. Conditionally applying a {@link StubbingStrategy} will result in the same behaviour as applying one that has
 * been created using {@link StubbingStrategy#when(Matcher)}.
 *
 * @see StubbingStrategy
 * @see RootStubber
 */
public interface RootStubberBuilder {

    /**
     * Adds an existing {@link RootStubber} as baseline for the {@link RootStubber} that is to be built. An included
     * {@link RootStubber} will be overridden by any other {@link RootStubber} that is added later, assuming the latter
     * {@link RootStubber} provides a conflicting stubbing strategy to stub a certain type. Any {@link RootStubber} will
     * be overridden by any configured {@link StubbingStrategy}.
     *
     * @param stubber a baseline {@link RootStubber}
     * @return this
     */
    RootStubberBuilder include(RootStubber stubber);

    /**
     * Adds a {@link StubbingStrategy}. An added {@link StubbingStrategy} will be overridden by any other {@link
     * StubbingStrategy} that is added later, assuming the latter {@link StubbingStrategy} accepts a types that is also
     * accepted by the former {@link StubbingStrategy}.
     *
     * @param strategy {@link StubbingStrategy} used to stub instances of matching types
     * @return {@code this}
     */
    RootStubberBuilder stubWith(StubbingStrategy strategy);

    /**
     * Adds a {@link StubbingStrategy} that will only be applied, if the given {@link Matcher} matches the type to be
     * stubbed.
     *
     * @param strategy {@link StubbingStrategy} used to stub instances of matching types
     * @param matcher  {@link Matcher} used as a prerequisite for applying the given {@link StubbingStrategy}
     * @return {@code this}
     */
    RootStubberBuilder stubWith(StubbingStrategy strategy, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link StubbingStrategy}s in iteration order according to the behaviour defined for {@link
     * RootStubberBuilder#stubWith(StubbingStrategy)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(StubbingStrategy)
     */
    RootStubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies);

    /**
     * Adds multiple conditional {@link StubbingStrategy}s in iteration order according to the behaviour defined for
     * {@link RootStubberBuilder#stubWith(StubbingStrategy)} and {@link RootStubberBuilder#stubWith(StubbingStrategy,
     * Matcher)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(Iterable)
     * @see RootStubberBuilder#stubWith(StubbingStrategy, Matcher)
     */
    RootStubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link StubbingStrategy}s in iteration order according to the behaviour defined for {@link
     * RootStubberBuilder#stubWith(StubbingStrategy)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @return {@code this}
     * @see RootStubberBuilder#stubWith(StubbingStrategy)
     */
    RootStubberBuilder stubWith(StubbingStrategy... strategies);

    /**
     * Builds a {@link RootStubber} instance that will attempt to stub an instance for a certain type using the
     * configured {@link RootStubber} baselines and {@link StubbingStrategy}s.
     *
     * @return a concrete {@link RootStubber} instance
     */
    RootStubber build();

}
