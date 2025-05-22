import org.example.Peano.Peano;
import org.example.Peano.PeanoError;
import org.example.Peano.PeanoImpl;
import org.example.Result;
import org.example.Result.Ok;
import org.example.Result.Error;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PeanoTest {
    @Test
    void testFromInt() {
        record TestCase(int input, Peano expected) {}

        final TestCase[] testCases = {
                new TestCase(0, new Peano.Zero()),
                new TestCase(1, new Peano.Succ(new Peano.Zero())),
                new TestCase(2, new Peano.Succ(new Peano.Succ(new Peano.Zero()))),
                new TestCase(3, new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero())))),
        };

        for (TestCase testCase : testCases) {
            Peano result = PeanoImpl.fromInt(testCase.input);
            assertEquals(result, testCase.expected);
        }
    }

    @Test
    void testToInt() {
        record TestCase(Peano input, int expected) {}

        final TestCase[] testCases = {
                new TestCase(new Peano.Zero(), 0),
                new TestCase(new Peano.Succ(new Peano.Zero()), 1),
                new TestCase(new Peano.Succ(new Peano.Succ(new Peano.Zero())), 2),
                new TestCase(new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero()))), 3),
        };

        for (TestCase testCase : testCases) {
            int result = PeanoImpl.toInt(testCase.input);
            assertEquals(result, testCase.expected);
        }
    }

    @Test
    void testAdd() {
        record TestCase(Peano p1, Peano p2, Peano expectedResult) {}

        final TestCase[] testCases = {
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(2)),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(5)),
                new TestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(5)),
        };

        for (TestCase testCase : testCases) {
            final Peano result = PeanoImpl.add(testCase.p1, testCase.p2);
            assertEquals(result, testCase.expectedResult);
        }
    }

    @Test
    void testMultiplication() {
        record TestCase(Peano p1, Peano p2, Peano expectedResult) {}

        final TestCase[] testCases = {
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(6)),
                new TestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(6)),
        };

        for (TestCase testCase : testCases) {
            final Peano result = PeanoImpl.mul(testCase.p1, testCase.p2);
            assertEquals(result, testCase.expectedResult);
        }
    }

    @Test
    void testSubtractionFailure() {
        record FailureTestCase (Peano p1, Peano p2, Error<Peano, PeanoError> expectedError) {}

        final FailureTestCase[] failureCases = {
            new FailureTestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Error<>(new PeanoError.CannotSubtractPositiveFromZero())),
            new FailureTestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(2), new Error<>(new PeanoError.CannotSubtractPositiveFromZero())),
        };

        for (FailureTestCase testCase : failureCases) {
            final Result<Peano, PeanoError> result = PeanoImpl.sub(testCase.p1, testCase.p2);

            switch (result) {
                case Ok<Peano, PeanoError> ok -> fail("Expected error but got success: " + ok.value());
                case Error<Peano, PeanoError> error -> assertEquals(error.error(), testCase.expectedError.error());
            }
        }
    }

    @Test
    void testSubtractionSuccess() {
        record SuccessTestCase (Peano p1, Peano p2, Ok<Peano, PeanoError> expectedResult) {}

        final SuccessTestCase[] successCases = {
            new SuccessTestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Ok<>(new Peano.Zero())),
            new SuccessTestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Ok<>(PeanoImpl.fromInt(1))),
            new SuccessTestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(1))),
            new SuccessTestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), new Ok<>(PeanoImpl.fromInt(1))),
        };

        for (SuccessTestCase testCase : successCases) {
            final Result<Peano, PeanoError> result = PeanoImpl.sub(testCase.p1, testCase.p2);

            switch (result) {
                case Error<Peano, PeanoError> error -> fail("Expected success but got error: " + error.error());
                case Ok<Peano, PeanoError> ok -> assertEquals(ok.value(), testCase.expectedResult.value());
            }
        }
    }

    @Test
    void testSubtractThenSum() {
        Result<Peano, PeanoError> subResult = PeanoImpl.sub(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2));

        // Compile error: Cannot convert from Result<Peano> to Peano
        // PeanoImpl.add(subResult, PeanoImpl.fromInt(2));
        // Forced to handle errors, without using exceptions in library code :)

        final Peano subbed = switch(subResult) {
            case Ok<Peano, PeanoError> ok -> ok.value();
            case Error<Peano, PeanoError> error -> fail("Unexpected error type: " + error.error());
        };
        assertEquals(1, PeanoImpl.toInt(subbed));

        final Peano result = PeanoImpl.add(subbed, PeanoImpl.fromInt(2));
        assertEquals(3, PeanoImpl.toInt(result));
    }
}
