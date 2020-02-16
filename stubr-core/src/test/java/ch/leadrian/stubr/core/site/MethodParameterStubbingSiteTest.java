package ch.leadrian.stubr.core.site;

import ch.leadrian.stubr.core.StubbingSite;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
class MethodParameterStubbingSiteTest {

    @Test
    void shouldReturnParent() throws Exception {
        StubbingSite expectedParent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter parameter = method.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(expectedParent, method, parameter);

        Optional<StubbingSite> parent = site.getParent();

        assertThat(parent)
                .hasValue(expectedParent);
    }

    @Test
    void shouldReturnMethod() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method expectedMethod = Foo.class.getMethod("foo", int.class);
        Parameter parameter = expectedMethod.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(parent, expectedMethod, parameter);

        Method method = site.getMethod();

        assertThat(method)
                .isEqualTo(expectedMethod);
    }

    @Test
    void shouldReturnMethodAsExecutable() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method expectedMethod = Foo.class.getMethod("foo", int.class);
        Parameter parameter = expectedMethod.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(parent, expectedMethod, parameter);

        Executable executable = site.getExecutable();

        assertThat(executable)
                .isEqualTo(expectedMethod);
    }

    @Test
    void shouldReturnParameter() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter expectedParameter = method.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(parent, method, expectedParameter);

        Parameter parameter = site.getParameter();

        assertThat(parameter)
                .isEqualTo(expectedParameter);
    }

    @Test
    void shouldReturnParameterIndex() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter parameter = method.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(parent, method, parameter);

        int index = site.getParameterIndex();

        assertThat(index)
                .isZero();
    }

    @Test
    void shouldReturnParameterAsAnnotatedElement() throws Exception {
        StubbingSite parent = mock(StubbingSite.class);
        Method method = Foo.class.getMethod("foo", int.class);
        Parameter expectedParameter = method.getParameters()[0];
        MethodParameterStubbingSite site = StubbingSites.methodParameter(parent, method, expectedParameter);

        AnnotatedElement annotatedElement = site.getAnnotatedElement();

        assertThat(annotatedElement)
                .isEqualTo(expectedParameter);
    }

    @Test
    void equalsShouldReturnTrueIfAndOnlyIfBothSitesAreEqual() throws Exception {
        StubbingSite parent1 = mock(StubbingSite.class);
        Method method1 = Foo.class.getMethod("foo", int.class);
        Parameter expectedParameter1 = method1.getParameters()[0];
        StubbingSite parent2 = mock(StubbingSite.class);
        Method method2 = Foo.class.getMethod("foo", String.class);
        Parameter expectedParameter2 = method2.getParameters()[0];

        new EqualsTester()
                .addEqualityGroup(StubbingSites.methodParameter(parent1, method1, expectedParameter1), StubbingSites.methodParameter(parent1, method1, expectedParameter1))
                .addEqualityGroup(StubbingSites.methodParameter(parent2, method2, expectedParameter2), StubbingSites.methodParameter(parent2, method2, expectedParameter2))
                .testEquals();
    }

    @SuppressWarnings("unused")
    public static final class Foo {

        public void foo(int param) {
        }

        public void foo(String param) {
        }

    }

}