import org.example.Ordering;
import org.example.Peano.Peano;
import org.example.Peano.Peano.Succ;
import org.example.Peano.Peano.Zero;
import org.example.Peano.PeanoDivisionError;
import org.example.Peano.PeanoConstructionError;
import org.example.Peano.PeanoImpl;
import org.example.Result;
import org.example.Result.Ok;
import org.example.Result.Err;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PeanoTest {
    @Test
    void testFromInt() {
        record TestData(int input, Peano expected) {}

        final TestData[] testCases = {
                new TestData(0, new Peano.Zero()),
                new TestData(1, new Peano.Succ(new Peano.Zero())),
                new TestData(2, new Peano.Succ(new Peano.Succ(new Peano.Zero()))),
                new TestData(3, new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero())))),
                new TestData(100, new Peano.Succ(PeanoImpl.fromInt(99))),
        };

        for (TestData testCase : testCases) {
            Peano result = PeanoImpl.fromInt(testCase.input);
            assertEquals(result, testCase.expected, "Failed for %d".formatted(testCase.input));
        }
    }

    @Test
    void testFromIntSafe() {
        record TestData(int input, Result<Peano, PeanoConstructionError> expected) {}

        final TestData[] testCases = {
                new TestData(0, new Ok<>(new Zero())),
                new TestData(1, new Ok<>(new Succ(new Zero()))),
                new TestData(2, new Ok<>(new Succ(new Succ(new Zero())))),
                new TestData(3, new Ok<>(new Succ(new Succ(new Succ(new Zero()))))),
                new TestData(100, new Ok<>(new Succ(PeanoImpl.fromInt(99)))),
                new TestData(-1, new Err<>(new PeanoConstructionError.Negative(-1))),
                new TestData(101, new Err<>(new PeanoConstructionError.TooLarge(101))),
        };

        for (TestData testCase : testCases) {
            Result<Peano, PeanoConstructionError> result = PeanoImpl.fromIntSafe(testCase.input);
            assertEquals(result, testCase.expected, "Failed for %d".formatted(testCase.input));
        }
    }

    @Test
    void testToInt() {
        record TestData(Peano input, int expected) {}

        final TestData[] testCases = {
                new TestData(new Peano.Zero(), 0),
                new TestData(new Peano.Succ(new Peano.Zero()), 1),
                new TestData(new Peano.Succ(new Peano.Succ(new Peano.Zero())), 2),
                new TestData(new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero()))), 3),
                new TestData(new Peano.Succ(PeanoImpl.fromInt(99)), 100),
        };

        for (TestData testCase : testCases) {
            int result = PeanoImpl.toInt(testCase.input);
            assertEquals(result, testCase.expected, "Failed for %s".formatted(testCase.input));
        }
    }

    @Test
    void testAddition() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(1)),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(2)),
                new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(5)),
                new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(5)),
        };

        for (TestData testCase : testCases) {
            final Peano result = PeanoImpl.add(testCase.p1, testCase.p2);
            assertEquals(result, testCase.expectedResult, "Failed for %s + %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testMultiplication() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(0)),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(6)),
                new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(6)),
        };

        for (TestData testCase : testCases) {
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
        record TestData(Peano p1, Peano p2, Result<Peano, PeanoDivisionError> expectedResult) {}

        final TestData[] testCases = {
                // division by 0
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Err<>(new PeanoDivisionError.DivisionByZero())),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Err<>(new PeanoDivisionError.DivisionByZero())),

                // division by 1
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(0))),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(1))),
                new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(2))),
                new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(1), new Ok<>(PeanoImpl.fromInt(3))),

                // division by 2, even and odd
                new TestData(PeanoImpl.fromInt(4), PeanoImpl.fromInt(2), new Ok<>(PeanoImpl.fromInt(2))),
                new TestData(PeanoImpl.fromInt(9), PeanoImpl.fromInt(4), new Ok<>(PeanoImpl.fromInt(2))),

                // divisor larger than dividend
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(2), new Ok<>(PeanoImpl.fromInt(0))),
                new TestData(PeanoImpl.fromInt(4), PeanoImpl.fromInt(5), new Ok<>(PeanoImpl.fromInt(0))),
        };

        for (TestData testCase : testCases) {
            final Result<Peano, PeanoDivisionError> result = PeanoImpl.div(testCase.p1, testCase.p2);
            assertEquals(testCase.expectedResult, result, "Failed for %s / %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testComparison() {
        record TestData(Peano p1, Peano p2, Ordering expectedResult) {}

        final TestData[] testCases = {
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Ordering.Equal()),
                new TestData(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Ordering.LessThan()),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Ordering.Greater()),
                new TestData(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), new Ordering.Equal()),
                new TestData(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), new Ordering.LessThan()),
                new TestData(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), new Ordering.Greater()),
        };

        for (TestData testCase : testCases) {
            final Ordering result = PeanoImpl.compare(testCase.p1, testCase.p2);
            assertEquals(testCase.expectedResult, result, "Failed for %s <=> %s".formatted(testCase.p1, testCase.p2));
        }
    }

    @Test
    void testDivideThenSum() {
        Result<Peano, PeanoDivisionError> subResult = PeanoImpl.div(PeanoImpl.fromInt(9), PeanoImpl.fromInt(2));

        // Compile error: Cannot convert from Result<Peano> to Peano
        // PeanoImpl.add(subResult, PeanoImpl.fromInt(2));
        // Forced to handle errors, without using exceptions in library code :)

        final Peano subbed = switch (subResult) {
            case Err(PeanoDivisionError error) -> fail("Unexpected error type: " + error);
            case Ok(Peano peano) -> peano;
        };
        assertEquals(4, PeanoImpl.toInt(subbed));

        final Peano result = PeanoImpl.add(subbed, PeanoImpl.fromInt(2));
        assertEquals(6, PeanoImpl.toInt(result));
    }
}
