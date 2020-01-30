package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface to indicate where an instance for a certain type is being stubbed. Common sites are {@link
 * ch.leadrian.stubr.core.site.ParameterStubbingSite} such as {@link ch.leadrian.stubr.core.site.MethodParameterStubbingSite}
 * or {@link ch.leadrian.stubr.core.site.ConstructorStubbingSite}. Further implementations can be found in {@link
 * ch.leadrian.stubr.core.site.StubbingSites}.
 * <p>
 * {@link Matcher}s may use to {@code StubbingSite} to determine whether a given {@link Stubber} should be applied.
 *
 * @see ch.leadrian.stubr.core.site.StubbingSites
 */
public interface StubbingSite {

    /**
     * Returns the parent of a {@code StubbingSite}.
     * <p>
     * Given the following two constructors for example:
     * <pre>
     * public Foo(Bar bar) {
     *     // ...
     * }
     *
     * public Bar(int value) {
     *     // ...
     * }
     * </pre>
     * If an instance of {@code Foo} is stubbed using {@link StubbingStrategies#constructor()}, it would require an
     * instance of {@code Bar} to create an instance. If {@code Bar} in turn is also created using {@link
     * StubbingStrategies#constructor()} then {@code Bar} instances would be created at a {@link
     * ch.leadrian.stubr.core.site.ConstructorParameterStubbingSite}. The parent of {@code Bar}'s site would be whatever
     * {@link StubbingSite} is the site of {@code Foo}.
     *
     * @return the parent site
     */
    Optional<? extends StubbingSite> getParent();

    /**
     * This method returns an ordered {@link Stream} of all transitive parents of and including {@code this}.
     * <p>
     * {@code this} will be the first element in the stream, followed by its parent, grandparent and so on until there
     * is a parent without a parent.
     *
     * @return a stream of all transitive parents of {@code this}
     */
    default Stream<StubbingSite> walk() {
        return getParent()
                .map(parent -> Stream.concat(Stream.of(this), parent.walk()))
                .orElseGet(() -> Stream.of(this));
    }

}
