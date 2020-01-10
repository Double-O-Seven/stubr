package ch.leadrian.stubr.core;

public interface RootStubberBuilder {

    RootStubberBuilder stubber(Stubber stubber);

    RootStubberBuilder stubber(Stubber stubber, TypeMatcher matcher);

    RootStubberBuilder stubber(Stubber stubber, ParameterMatcher matcher);

    RootStubberBuilder stubbers(Iterable<? extends Stubber> stubbers);

    RootStubberBuilder stubbers(Iterable<? extends Stubber> stubbers, TypeMatcher matcher);

    RootStubberBuilder stubbers(Iterable<? extends Stubber> stubbers, ParameterMatcher matcher);

    RootStubberBuilder stubbers(Stubber... stubbers);

    RootStubber build();

}
