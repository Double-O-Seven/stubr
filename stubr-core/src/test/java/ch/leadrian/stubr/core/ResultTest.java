package ch.leadrian.stubr.core;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SuppressWarnings("ALL")
class ResultTest {

    @Test
    void givenSuccessIsSuccessShouldReturnTrue() {
        Result<String> success = Result.success("Foo");

        assertThat(success.isSuccess())
                .isTrue();
    }

    @Test
    void givenSuccessIsFailureShouldReturnFalse() {
        Result<String> success = Result.success("Foo");

        assertThat(success.isFailure())
                .isFalse();
    }

    @Test
    void givenFailureIsSuccessShouldReturnFalse() {
        Result<String> failure = Result.failure();

        assertThat(failure.isSuccess())
                .isFalse();
    }

    @Test
    void givenFailureIsFailureShouldReturnTrue() {
        Result<String> failure = Result.failure();

        assertThat(failure.isFailure())
                .isTrue();
    }

    @Test
    void failuresShouldBeEqual() {
        Result<String> failure1 = Result.failure();
        Result<String> failure2 = Result.failure();

        assertThat(failure1)
                .isEqualTo(failure2);
    }

    @Test
    void successWithEqualValuesShouldBeEqual() {
        Result<String> success1 = Result.success("Foo");
        Result<String> success2 = Result.success("Foo");

        assertThat(success1)
                .isEqualTo(success2);
    }

    @Test
    void successWithDifferentValuesShouldBeEqual() {
        Result<String> success1 = Result.success("Foo");
        Result<String> success2 = Result.success("Bar");

        assertThat(success1)
                .isNotEqualTo(success2);
    }

    @Test
    void failureShouldNotBeEqualToSuccess() {
        Result<String> success = Result.success("Foo");
        Result<String> failure = Result.failure();

        assertThat(success)
                .isNotEqualTo(failure);
    }

    @Test
    void successShouldNotBeEqualToFailure() {
        Result<String> success = Result.success("Foo");
        Result<String> failure = Result.failure();

        assertThat(failure)
                .isNotEqualTo(success);
    }

    @Test
    void shouldMapSuccess() {
        Result<String> success = Result.success("Foo");

        Result<Integer> mappedResult = success.map(String::length);

        assertThat(mappedResult)
                .isEqualTo(Result.success(3));
    }

    @Test
    void shouldMapFailure() {
        Result<String> failure = Result.failure();

        Result<Integer> mappedResult = failure.map(String::length);

        assertThat(mappedResult)
                .isEqualTo(Result.failure());
    }

    @Test
    void givenSuccessItShouldReturnValue() {
        Result<String> success = Result.success("Foo");

        String value = success.getValue();

        assertThat(value)
                .isEqualTo("Foo");
    }

    @Test
    void givenFailureItShouldThrowExceptionOnGetValue() {
        Result<String> failure = Result.failure();

        Throwable caughtThrowable = catchThrowable(failure::getValue);

        assertThat(caughtThrowable)
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void givenFailureToStringShouldReturnClassName() {
        Result<String> failure = Result.failure();

        String string = failure.toString();

        assertThat(string)
                .isEqualTo("Failure");
    }

    @Test
    void givenSuccessToStringShouldReturnClassNameWithValue() {
        Result<String> failure = Result.success("foo");

        String string = failure.toString();

        assertThat(string)
                .isEqualTo("Success{value=foo}");
    }

    @Test
    void resultsShouldBeEqualIfAndOnlyIfBothAreFailureOrBothAreSuccessWithSameValue() {
        new EqualsTester()
                .addEqualityGroup(Result.failure(), Result.failure())
                .addEqualityGroup(Result.success("foo"), Result.success("foo"))
                .addEqualityGroup(Result.success("bar"), Result.success("bar"))
                .addEqualityGroup(Result.success(1337), Result.success(1337))
                .testEquals();
    }

}