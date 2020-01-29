package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

/**
 * A builder interface for {@link Stubber}.
 * <p>
 * A {@link Stubber} can be built by including other {@link Stubber}s as base and/or adding various {@link
 * StubbingStrategy} to stub specific types.
 * <p>
 * An included {@link Stubber} will be overridden by any other {@link Stubber} that is added later, assuming the latter
 * {@link Stubber} provides a conflicting stubbing strategy to stub a certain type. Any {@link Stubber} will be
 * overridden by any configured {@link StubbingStrategy}.
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
 * @see Stubber
 */
public interface StubberBuilder {

    /**
     * Adds an existing {@link Stubber} as baseline for the {@link Stubber} that is to be built. An included {@link
     * Stubber} will be overridden by any other {@link Stubber} that is added later, assuming the latter {@link Stubber}
     * provides a conflicting stubbing strategy to stub a certain type. Any {@link Stubber} will be overridden by any
     * configured {@link StubbingStrategy}.
     *
     * @param stubber a baseline {@link Stubber}
     * @return this
     */
    StubberBuilder include(Stubber stubber);

    /**
     * Adds a {@link StubbingStrategy}. An added {@link StubbingStrategy} will be overridden by any other {@link
     * StubbingStrategy} that is added later, assuming the latter {@link StubbingStrategy} accepts a types that is also
     * accepted by the former {@link StubbingStrategy}.
     *
     * @param strategy {@link StubbingStrategy} used to stub instances of matching types
     * @return {@code this}
     */
    StubberBuilder stubWith(StubbingStrategy strategy);

    /**
     * Adds a {@link StubbingStrategy} that will only be applied, if the given {@link Matcher} matches the type to be
     * stubbed.
     *
     * @param strategy {@link StubbingStrategy} used to stub instances of matching types
     * @param matcher  {@link Matcher} used as a prerequisite for applying the given {@link StubbingStrategy}
     * @return {@code this}
     */
    StubberBuilder stubWith(StubbingStrategy strategy, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link StubbingStrategy}s in iteration order according to the behaviour defined for {@link
     * StubberBuilder#stubWith(StubbingStrategy)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @return {@code this}
     * @see StubberBuilder#stubWith(StubbingStrategy)
     */
    StubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies);

    /**
     * Adds multiple conditional {@link StubbingStrategy}s in iteration order according to the behaviour defined for
     * {@link StubberBuilder#stubWith(StubbingStrategy)} and {@link StubberBuilder#stubWith(StubbingStrategy,
     * Matcher)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @param matcher    {@link Matcher} used as a prerequisite for applying the given {@link StubbingStrategy}
     * @return {@code this}
     * @see StubberBuilder#stubWith(Iterable)
     * @see StubberBuilder#stubWith(StubbingStrategy, Matcher)
     */
    StubberBuilder stubWith(Iterable<? extends StubbingStrategy> strategies, Matcher<? super Type> matcher);

    /**
     * Adds multiple {@link StubbingStrategy}s in iteration order according to the behaviour defined for {@link
     * StubberBuilder#stubWith(StubbingStrategy)}.
     *
     * @param strategies {@link StubbingStrategy}s used to stub instances of matching types
     * @return {@code this}
     * @see StubberBuilder#stubWith(StubbingStrategy)
     */
    StubberBuilder stubWith(StubbingStrategy... strategies);

    /**
     * Builds a {@link Stubber} instance that will attempt to stub an instance for a certain type using the configured
     * {@link Stubber} baselines and {@link StubbingStrategy}s.
     *
     * @return a concrete {@link Stubber} instance
     */
    Stubber build();

}
