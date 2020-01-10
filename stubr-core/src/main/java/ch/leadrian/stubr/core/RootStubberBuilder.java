package ch.leadrian.stubr.core;

public interface RootStubberBuilder {

    RootStubberBuilder stubber(Stubber stubber);

    RootStubberBuilder stubber(Stubber stubber, TypeMatcher matcher);

    RootStubberBuilder stubber(Stubber stubber, ParameterMatcher matcher);

    RootStubber build();

}
