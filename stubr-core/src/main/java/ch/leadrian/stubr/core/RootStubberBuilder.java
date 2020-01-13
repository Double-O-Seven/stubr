package ch.leadrian.stubr.core;

public interface RootStubberBuilder {

    RootStubberBuilder include(RootStubber rootStubber);

    RootStubberBuilder stubWith(Stubber stubber);

    RootStubberBuilder stubWith(Stubber stubber, TypeMatcher matcher);

    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers);

    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, TypeMatcher matcher);

    RootStubberBuilder stubWith(Stubber... stubbers);

    RootStubber build();

}
