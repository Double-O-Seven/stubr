package ch.leadrian.stubr.core;

import java.lang.reflect.Type;

public interface RootStubberBuilder {

    RootStubberBuilder include(RootStubber rootStubber);

    RootStubberBuilder stubWith(Stubber stubber);

    RootStubberBuilder stubWith(Stubber stubber, Matcher<? super Type> matcher);

    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers);

    RootStubberBuilder stubWith(Iterable<? extends Stubber> stubbers, Matcher<? super Type> matcher);

    RootStubberBuilder stubWith(Stubber... stubbers);

    RootStubber build();

}
