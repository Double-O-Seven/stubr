package ch.leadrian.stubr.integrationtest;

import ch.leadrian.stubr.core.RootStubber;
import ch.leadrian.stubr.integrationtest.testdata.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static ch.leadrian.stubr.core.RootStubbers.defaultRootStubber;
import static ch.leadrian.stubr.core.stubber.Stubbers.defaultCollections;
import static ch.leadrian.stubr.core.stubber.Stubbers.suppliedValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CoreIntegrationTest {

    @Nested
    class DefaultStubsTest {

        private TestData testData;

        @BeforeEach
        void setUp() {
            RootStubber rootStubber = RootStubber.builder()
                    .include(defaultRootStubber())
                    .stubWith(defaultCollections(3))
                    .stubWith(suppliedValue(String.class, sequenceNumber -> String.format("value%d", sequenceNumber)))
                    .stubWith(suppliedValue(Integer.class, sequenceNumber -> sequenceNumber))
                    .build();
            testData = rootStubber.stub(TestData.class);
        }

        @Test
        void shouldStubTestData() {
            assertAll(
                    () -> assertThat(testData.getPrimitives()).isNotNull(),
                    () -> assertThat(testData.getPrimitiveWrappers()).isNotNull(),
                    () -> assertThat(testData.getArrays()).isNotNull(),
                    () -> assertThat(testData.getCollections()).isNotNull(),
                    () -> assertThat(testData.getCommonDefaults()).isNotNull()
            );
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

    }

}
