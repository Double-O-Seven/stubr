package ch.leadrian.stubr.core;

import ch.leadrian.stubr.core.strategy.StubbingStrategies;

import java.lang.reflect.Type;

/**
 * This interface represents a strategy to stub a concrete type.
 * <p>
 * The default implementation of {@link Stubber} uses that first {@code StubbingStrategy} that accepts a given type and
 * therefore is able provide a suitable stub value for that type.
 * <p>
 * {@code StubbingStrategy} provides two methods that are related to each other:
 * <ul>
 * <li>{@link StubbingStrategy#accepts(StubbingContext, Type)}</li>
 * <li>{@link StubbingStrategy#stub(StubbingContext, Type)}</li>
 * </ul>
 * Any implementation of {@code StubbingStrategy} {@code accepts} and {@code stub} must satisfy the following criteria:
 * <br>
 * If {@code accepts} returns {@code true} for a given {@link Type} {@code T}, then, given the same {@link Type} {@code
 * T}, {@code stub} must return value that is an instance of {@code T}. If on the other hand {@code accepts} returns
 * {@code false}, it is NOT required that {@code stub} double-checks the input. The implementation may therefore assume
 * that the input is valid. Calling {@code stub} without checking the input with {@code accepts} first may therefore
 * result in undefined behaviour.
 */
public interface StubbingStrategy {

    /**
     * Method that determines whether {@code this} strategy can provide a suitable stub value for {@code type}, given
     * the {@code context}.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return {@code true} if the strategy can provide a suitable stub value for the given {@code type}, else {@code
     * false}
     */
    boolean accepts(StubbingContext context, Type type);

    /**
     * Returns a suitable value for the given {@code type}. The value may or may not be derived from the given {@link
     * StubbingContext} {@code context}. Since the given {@link StubbingContext} provides a reference to the {@link
     * Stubber} that invoked the method call, additional values required to create the stub value may be stubbed using
     * {@link StubbingContext#getStubber()}. Examples for this might be stubbing a value with a non-default constructor
     * or stubbing a non-empty collection.
     * <p>
     * Double-checking the input is not required and the implementation may assume that {@code stub} is only called, if
     * {@code accepts} has returned {@code true} for the same input {@link StubbingContext} and {@link Type}. Calling
     * {@code stub} without checking the input with {@code accepts} first may therefore * result in undefined
     * behaviour.
     *
     * @param context current stubbing context
     * @param type    type for which a stub value is requested
     * @return a suitable stub value for the given {@code type}
     */
    Object stub(StubbingContext context, Type type);

    /**
     * Creates a {@link StubbingStrategy} that is only applied both {@code this} and the given {@link Matcher} accept a
     * given {@link StubbingContext} and {@link Type}. The created stubbing strategy will delegate to {@code this} when
     * stubbing a value.
     *
     * @param typeMatcher an additional criteria for the returned {@link StubbingStrategy} to accepts a {@link
     *                    StubbingContext} and {@link Type}
     * @return a conditional {@link StubbingStrategy}
     */
    default StubbingStrategy when(Matcher<? super Type> typeMatcher) {
        return StubbingStrategies.conditional(this, typeMatcher);
    }

}
