package ch.leadrian.stubr.mockito;

import ch.leadrian.stubr.core.StubbingContext;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;

final class StubbingAnswer implements Answer<Object> {

    private final StubbingContext context;

    StubbingAnswer(StubbingContext context) {
        this.context = context;
    }

    @Override
    public Object answer(InvocationOnMock invocation) {
        Type returnType = invocation.getMethod().getReturnType();
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }
        MockitoStubbingSite site = new MockitoStubbingSite(context.getSite(), invocation);
        return context.getStubber().stub(returnType, site);
    }

}
