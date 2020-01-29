package ch.leadrian.stubr.integrationtest;

import ch.leadrian.stubr.core.Stubber;
import ch.leadrian.stubr.core.StubbingContext;
import ch.leadrian.stubr.core.site.AnnotatedStubbingSite;
import ch.leadrian.stubr.integrationtest.annotation.CollectionSize;
import ch.leadrian.stubr.integrationtest.testdata.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.leadrian.stubr.core.RootStubbers.defaultRootStubber;
import static ch.leadrian.stubr.core.matcher.Matchers.annotatedSiteIs;
import static ch.leadrian.stubr.core.matcher.Matchers.annotatedWith;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.collection;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.defaultCollections;
import static ch.leadrian.stubr.core.strategy.StubbingStrategies.suppliedValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertAll;

class CoreIntegrationTest {

    private TestData testData;

    @BeforeEach
    void setUp() {
        Stubber stubber = Stubber.builder()
                .include(defaultRootStubber())
                .stubWith(defaultCollections(3))
                .stubWith(suppliedValue(String.class, sequenceNumber -> String.format("value%d", sequenceNumber)))
                .stubWith(suppliedValue(Integer.class, sequenceNumber -> sequenceNumber))
                .stubWith(collection(List.class, ArrayList::new, this::getCollectionSize).when(annotatedSiteIs(annotatedWith(CollectionSize.class))))
                .build();
        testData = stubber.stub(TestData.class);
    }

    private int getCollectionSize(StubbingContext context) {
        AnnotatedStubbingSite annotatedSite = (AnnotatedStubbingSite) context.getSite();
        return annotatedSite.getAnnotatedElement().getAnnotation(CollectionSize.class).value();
    }

    @Test
    void shouldStubTestData() {
        assertAll(
                () -> assertThat(testData.getAnnotations()).isNotNull(),
                () -> assertThat(testData.getArrays()).isNotNull(),
                () -> assertThat(testData.getCollections()).isNotNull(),
                () -> assertThat(testData.getPrimitives()).isNotNull(),
                () -> assertThat(testData.getPrimitiveWrappers()).isNotNull()
        );
    }

    @Nested
    class ArraysTest {

        @Test
        void shouldStubBooleanArray() {
            assertThat(testData.getArrays().getBooleanArray()).containsExactly(false, false, false);
        }

        @Test
        void shouldStubByteArray() {
            assertThat(testData.getArrays().getByteArray()).containsExactly(0, 0, 0);
        }

        @Test
        void shouldStubShortArray() {
            assertThat(testData.getArrays().getShortArray()).containsExactly((short) 0, (short) 0, (short) 0);
        }

        @Test
        void shouldStubCharArray() {
            assertThat(testData.getArrays().getCharArray()).containsExactly('\0', '\0', '\0');
        }

        @Test
        void shouldStubIntArray() {
            assertThat(testData.getArrays().getIntArray()).containsExactly(0, 0, 0);
        }

        @Test
        void shouldStubLongArray() {
            assertThat(testData.getArrays().getLongArray()).containsExactly(0L, 0L, 0L);
        }

        @Test
        void shouldStubFloatArray() {
            assertThat(testData.getArrays().getFloatArray()).containsExactly(0f, 0f, 0f);
        }

        @Test
        void shouldStubDoubleArray() {
            assertThat(testData.getArrays().getDoubleArray()).containsExactly(0.0, 0.0, 0.0);
        }

        @Test
        void shouldStubStringArray() {
            assertThat(testData.getArrays().getStringArray()).containsExactly("value0", "value1", "value2");
        }

    }

    @Nested
    class AnnotationsTest {

        @Test
        void shouldReturnEmptyList() {
            assertThat(testData.getAnnotations().getEmptyList()).isEmpty();
        }

        @Test
        void shouldReturnListOfFiveStrings() {
            assertThat(testData.getAnnotations().getListOfFiveStrings()).containsExactly("value0", "value1", "value2", "value3", "value4");
        }

        @Test
        void shouldReturnListOfTwoStrings() {
            assertThat(testData.getAnnotations().getListOfTwoStrings()).containsExactly("value0", "value1");
        }

    }

    @Nested
    class CollectionsTest {

        @Test
        void shouldStubCollection() {
            assertThat(testData.getCollections().getCollection()).containsExactly("value0", "value1", "value2");
        }

        @Test
        void shouldStubList() {
            assertThat(testData.getCollections().getList()).containsExactly("value0", "value1", "value2");
        }

        @Test
        void shouldStubSet() {
            assertThat(testData.getCollections().getSet()).containsExactlyInAnyOrder("value0", "value1", "value2");
        }

        @Test
        void shouldStubNavigableSet() {
            assertThat(testData.getCollections().getNavigableSet()).containsExactly("value0", "value1", "value2");
        }

        @Test
        void shouldStubMap() {
            assertThat(testData.getCollections().getMap()).containsOnly(entry(0, "value0"), entry(1, "value1"), entry(2, "value2"));
        }

        @Test
        void shouldStubNavigableMap() {
            assertThat(testData.getCollections().getNavigableMap()).containsExactly(entry(0, "value0"), entry(1, "value1"), entry(2, "value2"));
        }

    }

    @Nested
    class PrimitivesTest {

        @Test
        void shouldStubBoolean() {
            assertThat(testData.getPrimitives().getBoolean()).isFalse();
        }

        @Test
        void shouldStubByte() {
            assertThat(testData.getPrimitives().getByte()).isZero();
        }

        @Test
        void shouldStubShort() {
            assertThat(testData.getPrimitives().getShort()).isZero();
        }

        @Test
        void shouldStubChar() {
            assertThat(testData.getPrimitives().getChar()).isEqualTo('\0');
        }

        @Test
        void shouldStubInt() {
            assertThat(testData.getPrimitives().getInt()).isZero();
        }

        @Test
        void shouldStubLong() {
            assertThat(testData.getPrimitives().getLong()).isZero();
        }

        @Test
        void shouldStubFloat() {
            assertThat(testData.getPrimitives().getFloat()).isZero();
        }

        @Test
        void shouldStubDouble() {
            assertThat(testData.getPrimitives().getDouble()).isZero();
        }

    }

    @Nested
    class PrimitiveWrappersTest {

        @Test
        void shouldStubBooleanWrapper() {
            assertThat(testData.getPrimitiveWrappers().getBoolean()).isFalse();
        }

        @Test
        void shouldStubByteWrapper() {
            assertThat(testData.getPrimitiveWrappers().getByte()).isZero();
        }

        @Test
        void shouldStubShortWrapper() {
            assertThat(testData.getPrimitiveWrappers().getShort()).isZero();
        }

        @Test
        void shouldStubCharWrapper() {
            assertThat(testData.getPrimitiveWrappers().getCharacter()).isEqualTo('\0');
        }

        @Test
        void shouldStubIntWrapper() {
            assertThat(testData.getPrimitiveWrappers().getInteger()).isZero();
        }

        @Test
        void shouldStubLongWrapper() {
            assertThat(testData.getPrimitiveWrappers().getLong()).isZero();
        }

        @Test
        void shouldStubFloatWrapper() {
            assertThat(testData.getPrimitiveWrappers().getFloat()).isZero();
        }

        @Test
        void shouldStubDoubleWrapper() {
            assertThat(testData.getPrimitiveWrappers().getDouble()).isZero();
        }

    }

}
