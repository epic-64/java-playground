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
            assertEquals(result, testCase.expected, "Failed for %d".formatted(testCase.input));
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
            assertEquals(result, testCase.expected, "Failed for %s".formatted(testCase.input));
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
            assertEquals(result, testCase.expectedResult, "Failed for %s + %s".formatted(testCase.p1, testCase.p2));
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
            assertEquals(result, testCase.expectedResult, "Failed for %s * %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testSubtraction() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
            new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(0)),
            new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(2), PeanoImpl.fromInt(0)),
            new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
            new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(1)),
            new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
            new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(1)),
        };

        for (TestData testCase : testCases) {
            final Peano result = PeanoImpl.sub(testCase.p1, testCase.p2);
            assertEquals(testCase.expectedResult, result, "Failed for %s - %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testDivision() {
        record TestData(Peano p1, Peano p2, Result<Peano, PeanoError> expectedResult) {}

        final TestData[] testCases = {
            // division by 0
            new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Error<>(new PeanoError.DivisionByZero())),
            new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Error<>(new PeanoError.DivisionByZero())),

            // division by 1
            new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(0))),
            new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(1))),
            new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(2))),
            new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(3))),

            // division by 2
            new TestData(PeanoImpl.fromInt(4), PeanoImpl.fromInt(2), new Ok<>(PeanoImpl.fromInt(2))),
            new TestData(PeanoImpl.fromInt(9), PeanoImpl.fromInt(4), new Ok<>(PeanoImpl.fromInt(2))),

            // todo: currently failing. fix it
            // divisor larger than dividend
            new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(2), new Ok<>(PeanoImpl.fromInt(0))),
        };

        for (TestData testCase : testCases) {
            final Result<Peano, PeanoError> result = PeanoImpl.div(testCase.p1, testCase.p2);
            assertEquals(testCase.expectedResult, result, "Failed for %s / %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testDivideThenSum() {
        Result<Peano, PeanoError> subResult = PeanoImpl.div(PeanoImpl.fromInt(9), PeanoImpl.fromInt(2));

        // Compile error: Cannot convert from Result<Peano> to Peano
        // PeanoImpl.add(subResult, PeanoImpl.fromInt(2));
        // Forced to handle errors, without using exceptions in library code :)

        final Peano subbed = switch(subResult) {
            case Ok<Peano, PeanoError> ok -> ok.value();
            case Error<Peano, PeanoError> error -> fail("Unexpected error type: " + error.error());
        };
        assertEquals(4, PeanoImpl.toInt(subbed));

        final Peano result = PeanoImpl.add(subbed, PeanoImpl.fromInt(2));
        assertEquals(6, PeanoImpl.toInt(result));
    }
}
