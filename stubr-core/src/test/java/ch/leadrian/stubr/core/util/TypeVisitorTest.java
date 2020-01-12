package ch.leadrian.stubr.core.util;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import static ch.leadrian.stubr.core.TypeTokens.getTypeArgument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("UnstableApiUsage")
class TypeVisitorTest {

    @Test
    void shouldVisitClass() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);

        TypeVisitor.accept(String.class, visitor);

        verify(visitor).visit(String.class);
    }

    @Test
    void shouldVisitParameterizedType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new TypeToken<List<String>>() {
        }.getType();

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((ParameterizedType) type);
    }

    @Test
    void shouldVisitWildcardType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        TypeToken<List<?>> token = new TypeToken<List<?>>() {
        };
        Type type = getTypeArgument(token, 0);

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((WildcardType) type);
    }

    @Test
    <T> void shouldVisitTypeVariable() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        TypeToken<List<T>> token = new TypeToken<List<T>>() {
        };
        Type type = getTypeArgument(token, 0);

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((TypeVariable<?>) type);
    }

    @Test
    <T> void shouldVisitGenericArrayType() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        TypeToken<List<T[]>> token = new TypeToken<List<T[]>>() {
        };
        Type type = getTypeArgument(token, 0);

        TypeVisitor.accept(type, visitor);

        verify(visitor).visit((GenericArrayType) type);
    }

    @Test
    void givenUnknownTypeItShouldThrowException() {
        @SuppressWarnings("unchecked")
        TypeVisitor<Void> visitor = mock(TypeVisitor.class);
        Type type = new UnknownType();

        Throwable caughtThrowable = catchThrowable(() -> TypeVisitor.accept(type, visitor));

        assertThat(caughtThrowable)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Unsupported type: UnknownType");
    }

    private static final class UnknownType implements Type {

        @Override
        public String toString() {
            return "UnknownType";
        }
    }

}