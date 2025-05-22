import org.example.Peano.Peano;
import org.example.Peano.PeanoImpl;
import org.example.Result;
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
        record FailureTestCase (Peano p1, Peano p2, Result.Error<IllegalArgumentException> expectedError) {}

        final FailureTestCase[] failureCases = {
            new FailureTestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Result.Error<>(new IllegalArgumentException("Cannot subtract a positive number from zero"))),
            new FailureTestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(2), new Result.Error<>(new IllegalArgumentException("Cannot subtract a positive number from zero"))),
        };

        for (FailureTestCase testCase : failureCases) {
            final Result<Peano> result = PeanoImpl.sub(testCase.p1, testCase.p2);

            switch (result) {
                case Result.Ok<Peano> ok -> fail("Expected error but got success: " + ok.getValue());
                case Result.Error<Peano> error -> assertEquals(error.getError().getMessage(), testCase.expectedError.getError().getMessage());
            }
        }
    }

    @Test
    void testSubtractionSuccess() {
        record SuccessTestCase (Peano p1, Peano p2, Result.Ok<Peano> expectedResult) {}

        final SuccessTestCase[] successCases = {
            new SuccessTestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Result.Ok<>(new Peano.Zero())),
            new SuccessTestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Result.Ok<>(PeanoImpl.fromInt(1))),
            new SuccessTestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), new Result.Ok<>(PeanoImpl.fromInt(1))),
            new SuccessTestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), new Result.Ok<>(PeanoImpl.fromInt(1))),
        };

        for (SuccessTestCase testCase : successCases) {
            final Result<Peano> result = PeanoImpl.sub(testCase.p1, testCase.p2);

            switch (result) {
                case Result.Error<Peano> error -> fail("Expected success but got error: " + error.getError().getMessage());
                case Result.Ok<Peano> ok -> assertEquals(ok.getValue(), testCase.expectedResult.getValue());
            }
        }
    }

    @Test
    void testSubtractThenSum() {
        final Peano subbed = switch(PeanoImpl.sub(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2))) {
            case Result.Ok<Peano> ok -> ok.getValue();
            case Result.Error<Peano> error -> fail("Expected success but got error: " + error.getError().getMessage());
        };
        assertEquals(1, PeanoImpl.toInt(subbed));

        final Peano result = PeanoImpl.add(subbed, PeanoImpl.fromInt(2));
        assertEquals(3, PeanoImpl.toInt(result));
    }
}
